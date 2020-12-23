-- ready(list) to working(hash)
local src = KEYS[1]
local dest = KEYS[2]
local currTime = ARGV[1]
local clientId = ARGV[2]

-- get and delete
local srcJson = redis.call('LPOP', src)
if (srcJson == false) then return end

-- update 'executionTime' and 'clientId' fields of json
local destTask = {}
local srcTask = cjson.decode(srcJson)
for k, v in pairs(srcTask) do
    destTask[k] = v
end
destTask['executionTime'] = currTime
destTask['clientId'] = clientId
local destJson = cjson.encode(destTask)

-- add
redis.call('HSET', dest, destTask['taskId'], destJson)

return destJson

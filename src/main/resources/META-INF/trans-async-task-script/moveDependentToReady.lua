-- hash -> list
local src = KEYS[1]
local dest = KEYS[2]
local taskId = ARGV[1]
local currTime = ARGV[2]

-- get
local srcJson = redis.call('HGET', src, taskId)
if (srcJson == false) then return false end

-- update 'readyTime' field of json
local destTask = {}
local srcTask = cjson.decode(srcJson)
for k, v in pairs(srcTask) do
    destTask[k] = v
end
destTask['readyTime'] = currTime
local destJson = cjson.encode(destTask)

-- add
if (redis.call('RPUSH', dest, destJson) == false) then return false end
-- delete
if (redis.call('HDEL', src, taskId) == false) then return false end
return true

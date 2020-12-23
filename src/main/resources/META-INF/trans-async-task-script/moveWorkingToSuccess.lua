-- working(hash) to success(list)
local src = KEYS[1]
local dest = KEYS[2]
local index = KEYS[3]
local cnt = KEYS[4]
local taskId = ARGV[1]
local currTime = ARGV[2]

-- get
local srcJson = redis.call('HGET', src, taskId)
if (srcJson == false) then return false end

-- update 'finishTime' field of json
local destTask = {}
local srcTask = cjson.decode(srcJson)
for k, v in pairs(srcTask) do
    destTask[k] = v
end
destTask['finishTime'] = currTime
local destJson = cjson.encode(destTask)

-- 1. add index-dependent-id
local dependId = destTask['selfDependId']
-- if index-dependent-id is not null, then add it (null field in json will be deserialized to userdata type)
if (type(dependId) == 'string') then
    if (redis.call('SADD', index, dependId) == false) then return false end
end
-- 2.1 add
if (redis.call('LPUSH', dest, destJson) == false) then return false end
-- 2.2 delete
if (redis.call('HDEL', src, taskId) == false) then return false end
-- 3.
redis.call('INCR', cnt)
return true

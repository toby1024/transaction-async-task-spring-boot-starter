local key = KEYS[1]
local origJson = ARGV[1]
local newScheduledTime = ARGV[2]

-- update 'scheduledTime' field of json
local newTask = {}
local origTask = cjson.decode(origJson)
for k, v in pairs(origTask) do
    newTask[k] = v
end
newTask['scheduledTime'] = newScheduledTime
local newJson = cjson.encode(newTask)

-- update member of zset:
--   1. remove
if (redis.call('ZREM', key, origJson) == 0) then return false end
--   2. add
if (redis.call('ZADD', key, newScheduledTime, newJson) == 0) then return false end
return true

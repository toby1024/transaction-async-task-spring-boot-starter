-- hash -> list
local src = KEYS[1]
local dest = KEYS[2]
local cnt = KEYS[3]
local taskId = ARGV[1]
local currTime = ARGV[2]

-- get
local srcJson = redis.call('HGET', src, taskId)
if (srcJson == false) then return false end

-- update 'deadTime' field of json
local destTask = {}
local srcTask = cjson.decode(srcJson)
for k, v in pairs(srcTask) do
    destTask[k] = v
end
destTask['deadTime'] = currTime
local destJson = cjson.encode(destTask)

-- 1.1 add
if (redis.call('LPUSH', dest, destJson) == false) then return false end
-- 1.2 delete
if (redis.call('HDEL', src, taskId) == false) then return false end
-- 2. insc count
redis.call('INCR', cnt)
return true


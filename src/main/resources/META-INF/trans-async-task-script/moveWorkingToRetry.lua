-- working(hash) to retry(zset)
local src = KEYS[1]
local dest = KEYS[2]
local taskId = ARGV[1]
local failedTime = ARGV[2]
local remainingRetry = ARGV[3]
local scheduledTime = ARGV[4]
local errMsg = ARGV[5]

-- get
local srcJson = redis.call('HGET', src, taskId)
if (srcJson == false) then return false end

-- update 'failedTime', 'remainingRetry' and 'scheduledTime' of json
local destTask = {}
local srcTask = cjson.decode(srcJson)
for k, v in pairs(srcTask) do
    destTask[k] = v
end
destTask['failedTime'] = failedTime
destTask['remainingRetry'] = remainingRetry
destTask['scheduledTime'] = scheduledTime
destTask['errMsg'] = errMsg
local destJson = cjson.encode(destTask)

-- add
if (redis.call('ZADD', dest, scheduledTime, destJson) == false) then return false end
-- delete
if (redis.call('HDEL', src, taskId) == false) then return false end
return true

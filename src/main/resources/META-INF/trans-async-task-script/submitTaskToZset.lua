local key_bizIds = KEYS[1]
local key_target = KEYS[2]
local bizId = ARGV[1]
local task = ARGV[2]
local scheduled_time = ARGV[3]

local r = 1
if (bizId ~= "optask-dummy-biz-id") then
    r = redis.call("SADD", key_bizIds, bizId)
end

if (r == 1) then
    return redis.call("ZADD", key_target, scheduled_time, task)
else
    return 2
end
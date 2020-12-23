local key_bizIds = KEYS[1]
local key_target = KEYS[2]
local bizId = ARGV[1]
local task = ARGV[2]

local r = 1
if (bizId ~= "optask-dummy-biz-id") then
    r = redis.call("SADD", key_bizIds, bizId)
end

if (r == 1) then
    local len = redis.call("RPUSH", key_target, task)
    if (len > 0) then
        return 1
    else
        return 0
    end
else
    return 2
end
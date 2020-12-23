local key_bizIds = KEYS[1]
local key_target = KEYS[2]
local bizId = ARGV[1]
local task = ARGV[2]
local taskId = ARGV[3]

local r = 1
if (bizId ~= "optask-dummy-biz-id") then
    r = redis.call("SADD", key_bizIds, bizId)
end

if (r == 1) then
    local resp = redis.call("HMSET", key_target, taskId, task)
    if resp['ok'] == 'OK' then return 1
    else return 0
    end
else
    return 2
end
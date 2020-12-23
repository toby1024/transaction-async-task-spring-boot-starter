local key_bizId = KEYS[1]
local key_delayed = KEYS[2]
local bizId = ARGV[1]
local taskJson = ARGV[2]

-- remove bizId
if (redis.call('SREM', key_bizId, bizId) == 0) then return false end
-- remove delayed task
if (redis.call('ZREM', key_delayed, taskJson) == 0) then return false end
return true

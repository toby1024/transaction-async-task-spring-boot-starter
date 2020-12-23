-- move from 'dead' list to 'ready' list
local key_dead = KEYS[1]
local key_ready = KEYS[2]
local srcJson = ARGV[1]
local currTime = ARGV[2]

-- 1. add into 'ready' list:
-- 1.1 update 'readyTime' field of json
local destTask = {}
local srcTask = cjson.decode(srcJson)
for k, v in pairs(srcTask) do
    destTask[k] = v
end
destTask['readyTime'] = currTime
local destJson = cjson.encode(destTask)
-- 1.2 add into 'ready' list
redis.call("RPUSH", key_ready, destJson)

-- 2. remove from 'dead' list
redis.call('LREM', key_dead, 0, srcJson)
return true


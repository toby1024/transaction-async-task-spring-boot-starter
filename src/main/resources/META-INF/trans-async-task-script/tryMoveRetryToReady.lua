local zset = KEYS[1]
local list = KEYS[2]
local currTime = ARGV[1]

-- get the element with min score
local t = redis.call('ZRANGE', zset, 0, 0, 'WITHSCORES')
if next(t) == nil then
    return nil
end

-- move if reaching the execution time
local srcJson = t[1]
local scheduledTime = t[2]
if tonumber(scheduledTime) <= tonumber(currTime) then
    -- update 'readyTime' field of json
    local destTask = {}
    local srcTask = cjson.decode(srcJson)
    for k, v in pairs(srcTask) do
        destTask[k] = v
    end
    destTask['readyTime'] = currTime
    local destJson = cjson.encode(destTask)

    -- move:
    -- add
    redis.call("RPUSH", list, destJson)
    -- delete
    redis.call('ZREMRANGEBYRANK', zset, 0, 0)
    return destTask['taskId']
end

local key = KEYS[1];
local userId = ARGV[1];

if (redis.call('hexists', key, userId) == 0) then
    return nil;
end;

local count = redis.call('hincrby', key, userId, -1);

if (count == 0) then
    redis.call('del', key);
    return nil;
end;
local key = KEYS[1];
local userId = ARGV[1];
local releaseTime = ARGV[2];

if(redis.call('exists', key) == 0) then
    redis.call('hset', key, userId, '1');
    redis.call('expire', key, releaseTime);
    return 1;
end;

if(redis.call('hexists', key, userId) == 1) then
    redis.call('hincrby', key, userId, '1');
    redis.call('expire', key, releaseTime);
    return 1;
end;
return 0;
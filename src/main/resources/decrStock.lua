local stock = tonumber(redis.call('get', KEYS[1]));
if (stock > 0) then
    redis.call('decrby', KEYS[1], 1);
    return stock;
end;
return 0;
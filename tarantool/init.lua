box.cfg{
    listen = 3301
}

box.once('bootstrap', function()
    box.schema.user.create('app', { password = 'app123', if_not_exists = true })
    box.schema.user.grant('app', 'read,write,execute', 'universe', nil, { if_not_exists = true })

    local s = box.schema.space.create('KV', { if_not_exists = true })

    s:format({
        { name = 'key', type = 'string' },
        { name = 'value', type = 'varbinary', is_nullable = true }
    })

    s:create_index('primary', {
        parts = {
            { field = 'key', type = 'string' }
        },
        if_not_exists = true
    })
end)

function kv_put(key, value)
    if value == nil then
        value = box.NULL
    end
    return box.space.KV:replace({key, value})
end

function kv_get(key)
    return box.space.KV:get(key)
end

function kv_delete(key)
    return box.space.KV:delete(key)
end

function kv_count()
    return box.space.KV:count()
end

function kv_range(from_key, to_key)
    local result = {}
    for _, tuple in box.space.KV.index.primary:pairs(from_key, { iterator = 'GE' }) do
        if tuple[1] > to_key then
            break
        end
        table.insert(result, {tuple[1], tuple[2]})
    end
    return result
end
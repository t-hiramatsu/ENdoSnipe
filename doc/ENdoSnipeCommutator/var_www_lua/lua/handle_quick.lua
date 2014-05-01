#! /usr/bin/lua

require "string"
local access_cnt = {}

function handle(r)
	r.content_type = "text/plain\n"

	if r.method == 'GET' then
		r:notice( string.format("uri: %s,1", r.uri))
		local cur_date = os.date("*t")
		r:notice( string.format("os.date: %d/%d/%d %d:%d:%d", cur_date.year, cur_date.month, cur_date.day, cur_date.hour, cur_date.min, cur_date.sec ))
		if r.handler ~= nil then
			r:notice( string.format("handler: %s", r.handler ))
		end
		if r.filename ~= nil then
			r:notice( string.format("filename: %s", r.filename))
		end

		-- r:puts("quick handler")
		return apache2.DECLINED
	else
		return 501
	end
	return apache2.OK
end

function quick_handle(r)
	local cur_str = get_cur_time()
	r:notice( string.format("quick handle : %s : uri: %s", cur_str, r.uri))

	return apache2.DECLINED
end

function fix_handle(r)
	local cur_str = get_cur_time()
	r:notice( string.format("fixups handle : %s : uri: %s", cur_str, r.uri))

	if access_cnt[r.uri] == nil then
		access_cnt[r.uri] = 1
	else
		access_cnt[r.uri] = access_cnt[r.uri] + 1;
	end

	r:notice( string.format("Access %s : count %d", r.uri, access_cnt[r.uri]));

--	write_to_dc("/tmp/access.log", cur_str .. "," .. r.uri)
	write_to_dc("/tmp/access.log", "/apache/access" .. r.uri .. ",1")
	return apache2.DECLINED
end

function write_to_dc(fname, str)
	local fh = io.open(fname, "a+")
	if fh then
		fh:write(str .. "\n")
		fh:flush()
		fh:close()
	end
end

function padzero(s, count)
	if s ~= nil then
		return string.rep("0", count-string.len(s)) .. s
	else
		return ""
	end
end


function get_cur_time()
	local cur_time = os.date("*t")
	local str_time = ""
	str_time = str_time .. padzero(cur_time.hour, 2)
	str_time = str_time .. padzero(cur_time.min, 2)
	str_time = str_time .. padzero(cur_time.sec, 2)

	return str_time
end

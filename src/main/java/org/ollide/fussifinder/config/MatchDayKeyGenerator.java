package org.ollide.fussifinder.config;

import org.jetbrains.annotations.NotNull;
import org.ollide.fussifinder.model.Period;
import org.ollide.fussifinder.model.Region;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Generates cache keys for the params {@link Region} and {@link Period}.
 * The format looks like <code>$REGION_TYPE:$REGION::$START_DATE$END_DATE</code>,
 * eg. <code>SPECIAL:hamburg_umland::2019-11-26:2019-11-26</code>.
 */
@Component("matchDayKeyGenerator")
public class MatchDayKeyGenerator implements KeyGenerator {

    @Override
    @NotNull
    public String generate(@NotNull Object target, @NotNull Method method, Object... params) {
        Region r = (Region) params[0];
        Period p = (Period) params[1];
        return generateKey(r, p);
    }

    public String generateKey(Region r, Period p) {
        return String.format("%s:%s::%s:%s", r.getType(), r.getName(), p.getStart().toString(), p.getEnd().toString());
    }
}

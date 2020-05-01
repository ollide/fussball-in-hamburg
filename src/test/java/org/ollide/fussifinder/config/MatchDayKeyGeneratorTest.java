package org.ollide.fussifinder.config;

import org.junit.jupiter.api.Test;
import org.ollide.fussifinder.model.Period;
import org.ollide.fussifinder.model.Region;
import org.ollide.fussifinder.model.RegionType;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MatchDayKeyGeneratorTest {

    @Test
    void generate() throws ClassNotFoundException {
        MatchDayKeyGenerator keyGenerator = new MatchDayKeyGenerator();

        Region hamburg = new Region(RegionType.CITY, "Hamburg");

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(6);
        Period period = new Period(start, end);

        // Some method...
        Method method = ReflectionUtils.findMethod(Class.forName("java.lang.Object"), "toString");

        String key = keyGenerator.generate(new Object(), method, hamburg, period);

        assertEquals(RegionType.CITY.name() + ":" + "Hamburg" + "::" + start.toString() + ":" + end.toString(), key);
    }

}

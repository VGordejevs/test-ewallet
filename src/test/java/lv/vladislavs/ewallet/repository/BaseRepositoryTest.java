package lv.vladislavs.ewallet.repository;

import lv.vladislavs.ewallet.configuration.JpaAuditingConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = ASSIGNABLE_TYPE,
        classes = {JpaAuditingConfig.class}
))
public class BaseRepositoryTest {
}

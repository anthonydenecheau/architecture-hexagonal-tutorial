package fr.scc.saillie.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import fr.scc.ddd.DomainService;
import fr.scc.ddd.Stub;
import fr.scc.saillie.spi.stubs.RaceInventoryStub;

@Configuration
@ComponentScan(
        basePackages = {"fr.scc.saillie"},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {DomainService.class,Stub.class})},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {RaceInventoryStub.class})})
public class DomainConfiguration {
}

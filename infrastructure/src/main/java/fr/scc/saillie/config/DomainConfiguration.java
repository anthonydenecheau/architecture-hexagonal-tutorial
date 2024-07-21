package fr.scc.saillie.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import fr.scc.saillie.ddd.DomainService;
import fr.scc.saillie.ddd.Stub;
import fr.scc.saillie.geniteur.spi.stubs.GeniteurInventoryStub;
import fr.scc.saillie.geniteur.spi.stubs.RaceInventoryStub;

@Configuration
@ComponentScan(
        basePackages = {"fr.scc.saillie.geniteur"},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {DomainService.class,Stub.class})},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {GeniteurInventoryStub.class, RaceInventoryStub.class})})
public class DomainConfiguration {
}

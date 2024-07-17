package fr.scc.saillie.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import fr.scc.ddd.DomainService;
import fr.scc.ddd.Stub;

@Configuration
@ComponentScan(
        basePackages = {"fr.scc.saillie"},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {DomainService.class,Stub.class})})
public class DomainConfiguration {
}

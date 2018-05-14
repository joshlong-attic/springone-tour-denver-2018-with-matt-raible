/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.security.oauth2.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientRegistrationRepositoryConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Configuration} used to map {@link OAuth2ClientProperties} to client
 * registrations.
 *
 * @author Madhura Bhave
 * @author Phillip Webb
 */
@Configuration
@EnableConfigurationProperties(OAuth2ClientProperties.class)
@Conditional(OAuth2ClientRegistrationRepositoryConfiguration.ClientsConfiguredCondition.class)
class ReactiveOAuth2ClientRegistrationRepositoryConfiguration {

	private final OAuth2ClientProperties properties;

	ReactiveOAuth2ClientRegistrationRepositoryConfiguration(OAuth2ClientProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean(ReactiveClientRegistrationRepository.class)
	public InMemoryReactiveClientRegistrationRepository clientRegistrationRepository() {
		List<ClientRegistration> registrations = new ArrayList<>(
				OAuth2ClientPropertiesRegistrationAdapter
						.getClientRegistrations(this.properties).values());
		return new InMemoryReactiveClientRegistrationRepository(registrations);
	}
}

/*
 * Copyright 2012-2018 the original author or authors.
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

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for OAuth client support.
 *
 * @author Madhura Bhave
 * @author Phillip Webb
 * @since 2.0.0
 */
@Configuration
@AutoConfigureBefore(name = "org.springframework.boot.autoconfigure.security.reactive.WebFluxSecurityConfiguration")
@ConditionalOnClass({ EnableWebFluxSecurity.class, ClientRegistration.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Import({ ReactiveOAuth2ClientRegistrationRepositoryConfiguration.class,
		ReactiveOAuth2WebSecurityConfiguration.class })
public class ReactiveOAuth2ClientAutoConfiguration {

}

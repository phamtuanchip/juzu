/*
 * Copyright 2013 eXo Platform SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package juzu.impl.bridge.request;

import junit.framework.Assert;
import juzu.test.AbstractWebTestCase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.Collections;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public abstract class AbstractRequestDetypedParam extends AbstractWebTestCase {

  /** . */
  public static String[] value;

  @Drone
  WebDriver driver;

  @Test
  public void testPathParam() throws Exception {
    driver.get(applicationURL().toString());
    WebElement trigger = driver.findElement(By.id("trigger"));
    value = null;
    trigger.click();
    Assert.assertNotNull(value);
    Assert.assertEquals(Collections.singletonList("detyped_value"), Arrays.<String>asList(value));
  }
}

/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package juzu.test.protocol.standalone;

import juzu.test.AbstractWebTestCase;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import java.net.URL;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public abstract class AbstractStandaloneTestCase extends AbstractWebTestCase {

  public static WebArchive createServletDeployment(String applicationName) {
    return createServletDeployment(false, applicationName);
  }

  public static WebArchive createServletDeployment(boolean asDefault, String... applicationNames) {

    // Create war
    WebArchive war = createDeployment(asDefault, applicationNames);

    // Descriptor
    URL descriptor = AbstractStandaloneTestCase.class.getResource("web.xml");
    war.setWebXML(descriptor);

    //
    return war;
  }
}

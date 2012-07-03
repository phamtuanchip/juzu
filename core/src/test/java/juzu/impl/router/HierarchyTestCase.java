/*
 * Copyright (C) 2010 eXo Platform SAS.
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

package juzu.impl.router;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static juzu.impl.router.metadata.DescriptorBuilder.*;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class HierarchyTestCase extends AbstractControllerTestCase {

  @Test
  public void testFoo() throws Exception {
    Router router = router().
        add(route("/a").with(routeParam("foo").withValue("bar")).
            sub(route("/b").with(routeParam("juu").withValue("daa")))).
        build();

    //
    // assertEquals(Collections.singletonMap(QualifiedName.create("foo"), "bar"), router.route("/a"));
    assertNull(router.route("/a"));

    //
    Map<QualifiedName, String> expected = new HashMap<QualifiedName, String>();
    expected.put(Names.FOO, "bar");
    expected.put(Names.JUU, "daa");
    assertEquals(expected, router.route("/a/b"));
  }
}

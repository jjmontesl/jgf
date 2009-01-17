/*
 * JGF - Java Game Framework
 * $Id$
 *
 * Copyright (c) 2008, JGF - Java Game Framework
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     * Neither the name of the 'JGF - Java Game Framework' nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <copyright holder> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <copyright holder> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.jgf.core.naming;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.jgf.config.ConfigException;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case.
 * @author jjmontes
 */
public final class TestDirectory {

	Directory directory;

	@Before
	public void setUp() throws Exception {
  	directory = new Directory();
  }

  @Test
  public void testInit() {
  	assertEquals(0, directory.getRetrievalCount());
  	assertEquals(0, directory.getPeakSize());
  }

  @Test
  public void testAddAndRetrieve() {
  	Integer a = new Integer(1);
  	Integer b = new Integer(2);
  	directory.addObject("integer/a", a);
  	directory.addObject("integer/b", b);

  	assertEquals(a, directory.getObjectAs("integer/a", Integer.class));
  	assertEquals(b, directory.getObjectAs("integer/b", Integer.class));
  }

  @Test
  public void testContains() {
  	Integer a = new Integer(1);
  	Integer b = new Integer(2);
  	directory.addObject("integer/a", a);
  	directory.addObject("integer/b", b);

  	assertTrue(directory.containsObject("integer/a"));
  	assertTrue(directory.containsObject("integer/b"));
  }

  @Test(expected=ConfigException.class)
  public void testReplace() {
  	Integer a1 = new Integer(1);
  	Integer a2 = new Integer(2);

  	directory.addObject("integer/a", a1);
  	directory.addObject("integer/a", a2);
  }

  @Test
  public void testPeakSize() {
  	Integer a = new Integer(1);
  	Integer b = new Integer(2);
  	Integer c = new Integer(3);

  	directory.addObject("integer/a", a);
  	assertEquals(1, directory.getPeakSize());

  	directory.addObject("integer/b", b);
  	assertEquals(2, directory.getPeakSize());

  	directory.addObject("integer/c", c);
  	assertEquals(3, directory.getPeakSize());

  	directory.removeObject("integer/b");
  	assertEquals(3, directory.getPeakSize());
  }

  @Test(expected=ConfigException.class)
  public void testRemove() {
  	Integer a = new Integer(1);
  	directory.addObject("integer/a", a);
  	directory.removeObject("integer/a");
  	directory.getObjectAs("integer/a", Integer.class);
  }

  @Test
  public void testExcludingPrefix() {
  	Integer a = new Integer(1);
  	directory.addObject("!excludedEntry", a);
  	assertFalse(directory.containsObject("!excludedEntry"));
  	assertFalse(directory.containsObject("excludedEntry"));
  }

  @Test(expected=ConfigException.class)
  public void testBlankName() {
  	directory.addObject("", new Integer(1));
  }

  @Test(expected=ConfigException.class)
  public void testNullName() {
  	directory.addObject(null, new Integer(1));
  }

  @Test(expected=ConfigException.class)
  public void testNullObject() {
  	directory.addObject("nullObject", null);
  }

}

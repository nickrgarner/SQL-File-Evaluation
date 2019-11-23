/*
 * TestRowCountTests - JUnit test case class to test TestRowCount class
 * 
 * Created - Paul J. Wagner, 18-Oct-2017
 */
package edu.uwec.cs.wagnerpj.sqltest.junit;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uwec.cs.wagnerpj.sqltest.sqltests.TestRowCount;

public class TestRowCountTests extends AbstractTest {
	// data
	TestRowCount testRC = null;

	@Before
	public void setup() {
		testRC = new TestRowCount();
	}

	@Test
	public void testTestRowCount() {
		// queries of same table but selecting different # of columns have same row count
		assertEquals(testRC.sqlTest(testDAO, creatureAllQuery, creatureOneColQuery.toString()), 10);
		// queries of different tables with different numbers of rows do not have same row count
		assertEquals(testRC.sqlTest(testDAO, creatureAllQuery, achievementAllQuery.toString()), 0);
		// queries of different table with same number of rows does have same row count
		assertEquals(testRC.sqlTest(testDAO, achievementAllQuery, aspirationAllQuery.toString()), 10);
		// queries generating 1 and 0 creatures, respectively, should generate a partial score
		assertEquals(testRC.sqlTest(testDAO, creatureOneRowQuery, creatureZeroQuery.toString()), 5);
		// a valid query and a null query should not have the same row count
		assertEquals(testRC.sqlTest(testDAO, creatureAllQuery, nullQuery.toString()), 0);
		// a valid table query and bad query are not equal in row count
		assertEquals(testRC.sqlTest(testDAO, creatureAllQuery, badQuery.toString()), 0);
	}
	
	@After
	public void teardown () {
		testRC = null;
	}
	
}	// end - test case class TestRowCountTests
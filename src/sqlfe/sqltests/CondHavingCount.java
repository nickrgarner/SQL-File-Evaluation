/*
 * CondHavingCount - class to evaluate condition for query 'having' count
 * 
 * Created - Paul J. Wagner, 1-Oct-2018
 */
package sqlfe.sqltests;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import sqlfe.general.IDAO;
import sqlfe.general.Query;
import sqlfe.general.TestResult;
import sqlfe.util.Utilities;

public class CondHavingCount implements ISQLTest {
	public TestResult sqlTest (IDAO dao, Query givenQuery, String condition) {
		int result;						// result on scale 0 to 10
		int thisHavingCt = -1;			// having count returned from this query
		boolean compResult = false;		// result of condition evaluation
		
		// count number of havings in query
		thisHavingCt = Utilities.countMatches(givenQuery.toString(), "HAVING ") +
		               Utilities.countMatches(givenQuery.toString(), "HAVING(") +
        			   Utilities.countMatches(givenQuery.toString(), "HAVING\r\n") +
        			   Utilities.countMatches(givenQuery.toString(), "HAVING\n");
		// build full condition from string condition
		String fullCondition = (thisHavingCt + condition);
		
		// evaluate full condition
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		try {
			compResult = (Boolean)engine.eval(fullCondition);
		}
		catch (ScriptException se) {
			System.err.println("CondHavingCount - cannot evaluate condition");
		}
		
		// compare and generate result
		result = compResult ? 10 : 0;
		
		return new TestResult(result);
	}	// end - condHavingCount
	
	public String getName() {
		return "CondHavingCount";
	}
	
	public String getDesc() {
		return "Answer has appropriate number of HAVING keyword";
	}
}	// end - class CondHavingCount

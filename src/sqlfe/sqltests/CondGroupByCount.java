/*
 * CondGroupByCount - class to evaluate condition for query 'group by' count
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

public class CondGroupByCount implements ISQLTest {
	public TestResult sqlTest (IDAO dao, Query givenQuery, String condition) {
		int result;						// result on scale 0 to 10
		int thisGroupByCt = -1;			// group by count returned from this query
		boolean compResult = false;		// result of condition evaluation
		
		// count number of group bys in query
		thisGroupByCt = Utilities.countMatches(givenQuery.toString(), "GROUP BY ") +
						Utilities.countMatches(givenQuery.toString(), "GROUP BY(") +
						Utilities.countMatches(givenQuery.toString(), "GROUP BY\r\n") +
						Utilities.countMatches(givenQuery.toString(), "GROUP BY\n");
		// build full condition from string condition
		String fullCondition = (thisGroupByCt + condition);
		
		// evaluate full condition
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		try {
			compResult = (Boolean)engine.eval(fullCondition);
		}
		catch (ScriptException se) {
			System.err.println("CondGroupByCount - cannot evaluate condition");
		}
		
		// compare and generate result
		result = compResult ? 10 : 0;
		
		return new TestResult(result);
	}	// end - condGroupByCount
	
	public String getName() {
		return "CondGroupByCount";
	}
	
	public String getDesc() {
		return "Answer has appropriate number of GROUP BY keywords";
	}
}	// end - class CondGroupByCount

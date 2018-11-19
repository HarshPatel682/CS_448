import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class CC
{

	/**
	 * Execute all given transaction using a 2PL protocol.
	 * Each {@code String} in the {@code transactions} list represents a transaction. 
	 * A transaccion includes an ordered list of operations separated by a semicolor (;).  
	 * No operation in a transaction can be executed out of order, but operations may be 
	 * interleaved with other transactions if allowed by the locking protocol. 
	 *
	 * Print the system log to either the console or a file at the end of the method.
	 *
	 * @param db the initial state of the database
	 *                (e.g. "A", "B", etc).
	 * @param transactions  the list of transactions. The index of the transaction in the 
	 *                list is equivalent to the transaction ID. For example, for an input: 
	 *                "W(1,5);R(2);W(2,3);R(1);C","R(1);W(1,2);C", the corresponding 
	 *                transactions are:
	 *                  T1: "W(1,5);R(2);W(2,3);R(1);C"
	 *                  T2: "R(1);W(1,2);C"
	 * @return  the {@code db} state after excuting the transactions.
	 */


	public static int[] executeSchedule(int[] db, List<String> transactions)
	{
		//TODO

		String execution = round_robin(transactions);
		//System.out.println(execution);
		return null;
	}

	public static String round_robin(List<String> transactions) {
		String result = "";

		Hashtable<String, List<String>> transactions_t = new Hashtable<>();
		String curr = "T";
		int i = 1;
		for (String s: transactions ) {
			String[] splits = s.split(";");
			String temp = curr + i;
			if (!transactions_t.containsKey(temp)) {
				transactions_t.put(temp, new ArrayList<String>());
			}
			for (int j = 0; j < splits.length; j++) {
				transactions_t.get(temp).add(splits[j]);
			}
			i++;
		}

		
		return null;
	}

	//this will generate a system log string to be added to the entire log
	public static String system_log(String log) {
		return null;
	}
}
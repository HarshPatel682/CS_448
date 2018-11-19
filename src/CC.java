import java.util.*;

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

		String execution = round_robin(db, transactions);
		//System.out.println(execution);
		return null;
	}

	public static String round_robin(int[] db, List<String> transactions) {
		String result = "";
		HashMap<Integer, String> locks = new HashMap<>();

		int max_actions = 0;
		for (int i = 0; i < transactions.size(); i++) {
			String[] splits = transactions.get(i).split(";");
			if (splits.length > max_actions) {
				max_actions = splits.length;
			}
		}
		//rows are the T1 (T2, T3, etc), and columns are the actions for each
		// i x j
		String[][] table = new String[transactions.size()][max_actions];

		int row = 0;
		int column;
		String empty = "0";
		for (String s:transactions ) {
			String[] splits = s.split(";");
			for (column = 0; column < max_actions; column++) {
				if (column < splits.length) {
					table[row][column] = splits[column];
				}
			}
			row++;
		}

		String t = "T";
		HashMap<String, Integer> trans_ids = new HashMap<>();
		for (int i = 0; i < table.length; i++) {
			trans_ids.put(t + "" + (i+1), 0);
		}

		//using table[j][i]
//		for (int i = 0; i <	table[0].length; i++) {
//			String t_id;
//			for (int j = 0; j < table.length; j++) {
//				t_id = "T" + (j+1);
//				int index = trans_ids.get(t_id);
//				String temp = table[j][index];
//				if (temp.contains("W")) {
//					int id = Integer.parseInt(temp.substring(temp.indexOf('(')+1, temp.indexOf(',')));
//					if (locks.get(id) == null || locks.get(id).equals("0")) {
//						locks.put(id, t_id);
//						trans_ids.put(t_id, trans_ids.get(t_id)+1);
//						result += t_id + ":" +temp + ";";
//					}
//				} else if (temp.contains("R")) {
//					int id = Integer.parseInt(temp.substring(temp.indexOf('(')+1, temp.indexOf(')')));
//					if (locks.get(id) == null || locks.get(id).equals(t_id)) {
//						trans_ids.put(t_id, trans_ids.get(t_id)+1);
//						result += t_id + ":" +temp + ";";
//					}
//				}
//			}
//		}

		int num_commits = 0;
		while (num_commits < table.length) {
			String t_id;
			for (int j = 0; j < table.length; j++) {
				t_id = "T" + (j+1);
				int index = trans_ids.get(t_id);
				String temp = table[j][index];
				String quick_check = t_id + ":" +temp + ";";
				if (result.contains(quick_check)) {
					continue;
				}
				if (temp.contains("W")) {
					int id = Integer.parseInt(temp.substring(temp.indexOf('(')+1, temp.indexOf(',')));
					if (locks.get(id) == null || locks.get(id).equals("0")) {
						locks.put(id, t_id);
						trans_ids.put(t_id, trans_ids.get(t_id)+1);
						result += quick_check;
					}
				} else if (temp.contains("R")) {
					int id = Integer.parseInt(temp.substring(temp.indexOf('(')+1, temp.indexOf(')')));
					if (locks.get(id) == null || locks.get(id).equals(t_id)) {
						trans_ids.put(t_id, trans_ids.get(t_id)+1);
						result += quick_check;
					}
				} else if (temp.equals("C")) {
					num_commits++;
					result += quick_check;
					Iterator<Map.Entry<Integer,String>> iter = locks.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry<Integer,String> entry = iter.next();
						if(t_id.equalsIgnoreCase(entry.getValue())){
							iter.remove();
						}
					}
				}
			}
		}
		System.out.println(result);
		return null;
	}

	//TABLE IMPLEMENTATION...
//	int max_actions = 0;
//		for (int i = 0; i < transactions.size(); i++) {
//	String[] splits = transactions.get(i).split(";");
//	if (splits.length > max_actions) {
//		max_actions = splits.length;
//	}
//}
//	//rows are the T1 (T2, T3, etc), and columns are the actions for each
//	// i x j
//	String[][] table = new String[transactions.size()][max_actions];
//
//	int row = 0;
//	int column;
//	String empty = "0";
//		for (String s:transactions ) {
//	String[] splits = s.split(";");
//	for (column = 0; column < max_actions; column++) {
//		if (column < splits.length) {
//			table[row][column] = splits[column];
//		}
//	}
//	row++;
//}
//
//	String t = "T";
//	//using table[j][i]
//	int i = 0;
//	int j = 0;

	//this will generate a system log string to be added to the entire log
	public static String system_log(String log) {
		return null;
	}
}
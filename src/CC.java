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

	private static int[] database;
    private static int timestamp;
    private static List<String> complete_system_log;
    private static HashMap<String, Integer> previous_timestamp;

    public static int[] executeSchedule(int[] db, List<String> transactions)
	{
		//TODO
        database = db.clone();
        timestamp = 0;
        complete_system_log = new ArrayList<>();
        previous_timestamp = new HashMap<>();

		String execution = round_robin(db, transactions);
		String[] splits = execution.split(";");
		for (String s: splits) {
			system_log(s);
		}

        System.out.println("Log:");
        for (String s: complete_system_log) {
            System.out.println(s);
        }
		return database;
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
		//System.out.println(result);
		return result;
	}


	//this will generate a system log string to be added to the entire log
	public static void system_log(String log) {
        String s = log;
            String Tid = s.substring(0, s.indexOf(":"));

            if (!previous_timestamp.containsKey(Tid)) {
                previous_timestamp.put(Tid, -1);
            }

            String command = s.substring(s.indexOf(":")+1, s.indexOf(":")+2);

            String to_add = command + ":"+ timestamp + "," + Tid;
            if (command.equals("W")) {
                int recordID = Integer.parseInt(s.substring(s.indexOf("(")+1, s.indexOf(",")));
                int new_value = Integer.parseInt(s.substring(s.indexOf(",")+1, s.indexOf(")")));

                int old_value = database[recordID];

                 to_add += ","+ recordID + "," + old_value + "," + new_value + "," + previous_timestamp.get(Tid);
                complete_system_log.add(to_add);
                previous_timestamp.put(Tid, timestamp);

                database[recordID] = new_value;

            } else if (command.equals("R")) {

                int recordID = Integer.parseInt(s.substring(s.indexOf("(")+1, s.indexOf(")")));
                int value_read = database[recordID];

                to_add += ","+ recordID + "," + value_read + "," + previous_timestamp.get(Tid);
                complete_system_log.add(to_add);

                previous_timestamp.put(Tid, timestamp);

            } else if (command.equals("C")) {
                to_add += "," + previous_timestamp.get(Tid);
                complete_system_log.add(to_add);
                previous_timestamp.put(Tid, timestamp);
            }
            timestamp++;

	}
}
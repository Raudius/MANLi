package us.raudi.manli.examples;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryonet.Connection;

import us.raudi.manli.Amend;
import us.raudi.manli.Model;

public class SimpleQuiz extends Model{
	private ConcurrentHashMap<Integer, String> clients = new ConcurrentHashMap<>();
	
	private LinkedList<String> buzzed_players = new LinkedList<>();
	private LinkedList<Long> buzzed_times = new LinkedList<>();
	
	private long startTime = System.currentTimeMillis();


	@Override
	public void register(Kryo k) {
		k.register(SimpleQuiz.class);
		k.register(ConcurrentHashMap.class);
		k.register(LinkedList.class, new CollectionSerializer());
	}
	
	@Override
	public void addClient(Connection con) {
		clients.put(con.getID(), "");
	}

	@Override
	public void removeClient(Connection con) {
		clients.remove(con.getID());
	}	

	@Override
	public boolean update(Amend data) {
		int clientID = data.getClientID();
		boolean newPlayer = clients.put(clientID, data.getString("name")).equals("");
		boolean isBuzz = buzz(data);
		
		return newPlayer || isBuzz;
	}
	
	private boolean buzz(Amend data) {
		String name = data.getString("name");
		
		boolean isBuzz = data.getBool("buzz") == null ? false : data.getBool("buzz");
		System.out.println(name + " :: " + isBuzz);
		
		if(!isStarted())  return false;
		
		
		// if no buzz or player already buzzed
		if(!isBuzz || buzzed_players.contains(name))  
			return false;
		
		addBuzzed(name);
		
		return true;
	}
	
	
	
	private void addBuzzed(String name) {
		buzzed_players.add(name);
		buzzed_times.add(elapsedTime());
	}

	public void start() {
		buzzed_players.clear();
		buzzed_times.clear();
		startTime = System.currentTimeMillis();
	}
	
	public void stop() {
		startTime = -1;
	}
	
	public void toggle() {
		if(isStarted()) stop();
		else start();
	}

	public boolean isStarted() {
		return startTime > 0;
	}

	public Long elapsedTime() {
		if(startTime < 0) return startTime;
		
		return System.currentTimeMillis() - startTime;
	}
	
	public String elapsedTimeString() {
		return timeToString(elapsedTime());
	}
	
	
	private String timeToString(Long t) {
		t = Math.max(t, 0);
		
		long ms = t % 1000;
		long second = (t / 1000) % 60;
		long minute = (t / (1000 * 60)) % 60;
		long hour = (t / (1000 * 60 * 60)) % 24;

		if(hour == 0)
			return String.format("%02d:%02d:%03d", minute, second, ms);
		else
			return String.format("%02d:%02d:%02d", hour, minute, second);
	}
	
	
	public class BuzzRecord {
		public String name;
		public String time;
		
		public BuzzRecord(String name, String time) {
			this.name = name;
			this.time = time;
		}
	}
	
	public ArrayList<BuzzRecord> getBuzzRecords(){
		ArrayList<BuzzRecord> records = new ArrayList<>();
		HashSet<String> added = new HashSet<>();
		
		for(int i=0; i<buzzed_players.size(); i++) {
			String player = buzzed_players.get(i);
			String time = timeToString(buzzed_times.get(i));
			records.add(new BuzzRecord(player, time));
			added.add(player);
		}
		
		for(String player : clients.values()) {
			if(added.contains(player)) continue;
								
			records.add(new BuzzRecord(player, ""));
			added.add(player);
		}
		
		return records;
	}
	
	
	

	@Override
	public String toString() {
		String str = "\n\n\n\n\n\n\n\n";
		str += "==============================================\n";
		for(String player : clients.values())
			str += String.format("> %s    (%s)  < \n", player, buzzed_players.contains(player));
		str += "==============================================\n";
		return str;
	}
}

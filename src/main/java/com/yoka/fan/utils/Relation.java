package com.yoka.fan.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.yoka.fan.network.GetFans;
import com.yoka.fan.network.GetFollower;
import com.yoka.fan.network.Request.Status;

public class Relation implements Serializable{

	private ArrayList<String> followers;

	private ArrayList<String> fans;

	private Relation() {

	}
	
	private static Relation instance;

	public ArrayList<String> getFollowers() {
		return followers;
	}

	public ArrayList<String> getFans() {
		return fans;
	}

	public static interface OperatorListener<T> {
		public void success(T result);
	}
	
	public synchronized static void read(User user,final OperatorListener<Relation> listener) {
		if (instance != null) {
			if (listener != null) {
				listener.success(instance);
				return;
			}
		}
		Relation relation = readFile();
		if(relation != null){
			instance = relation;
			if(listener != null){
				listener.success(relation);
				return;
			}
		}
		sync(user,new OperatorListener<Relation>() {

			@Override
			public void success(Relation relation) {
				instance = relation;
				if (listener != null) {
					listener.success(instance);
				}

			}
		});

	}
	
	public synchronized static void sync(final User user,final OperatorListener<Relation> listener) {
		
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				GetFans request = new GetFans(user.id,user.id,0,1000);
				request.request();
				GetFollower follower = new GetFollower(user.id, user.id, 0, 1000);
				follower.request();
				if (request.getStatus() == Status.SUCCESS && follower.getStatus() == Status.SUCCESS) {
					Relation relation = new Relation();
					relation.fans = (ArrayList<String>) request.getIdList();
					relation.followers = (ArrayList<String>) follower.getIdList();
					save(relation);
					if (listener != null) {
						listener.success(relation);
					}
				}

			}
		});
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}

	private synchronized static void save(final Relation relation) {
		instance = relation;
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					FileOutputStream fout = new FileOutputStream(
							Dirctionary.getRelationObjectFile());
					ObjectOutputStream oos = new ObjectOutputStream(fout);
					oos.writeObject(relation);
					oos.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});
		thread.setPriority(Thread.NORM_PRIORITY - 2);
		thread.start();

	}
	
	private synchronized static Relation readFile(){
		Relation relation = null;
		ObjectInputStream oi = null;
		try {
			FileInputStream fi = new FileInputStream(
					Dirctionary.getRelationObjectFile());
			oi = new ObjectInputStream(fi);
			relation = (Relation) oi.readObject();
		} catch (Exception e) {
//			Dirctionary.getCateObjectFile().delete();
			e.printStackTrace();
		}finally{
			try {
				if(oi != null){
					oi.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return relation;
	}
	
	public synchronized static void findFans(User user,final String id,final OperatorListener<Boolean> listener){
		read(user,new OperatorListener<Relation>() {

			@Override
			public void success(Relation result) {
				boolean exist = result.fans.indexOf(id) != -1 ? true :false;
				if(listener != null){
					listener.success(exist);
				}
			}
			
			
		});
	}
	
	public synchronized static void findFollower(User user,final String id,final OperatorListener<Boolean> listener){
		read(user,new OperatorListener<Relation>() {

			@Override
			public void success(Relation result) {
				boolean exist = result.followers.indexOf(id) != -1 ? true :false;
				if(listener != null){
					listener.success(exist);
				}
			}
			
			
		});
	}
	
	public synchronized static void addFans(User user, final String id){
		read(user,  new OperatorListener<Relation>() {

			@Override
			public void success(Relation result) {
				result.fans.add(id);
				save(result);
			}
			
		});
	}
	
	public synchronized static void removeFans(User user,final String id){
		read(user,  new OperatorListener<Relation>() {

			@Override
			public void success(Relation result) {
				result.fans.remove(id);
				save(result);
			}
		});
	}

}

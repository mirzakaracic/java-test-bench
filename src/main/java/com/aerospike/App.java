package com.aerospike;

import java.util.ArrayList;

import com.aerospike.client.*;
import com.aerospike.client.lua.LuaStreamLib.read;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.Policy;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        var clientPolicy = new ClientPolicy();
        clientPolicy.timeout = 500;
        clientPolicy.failIfNotConnected = true;
        clientPolicy.maxConnsPerNode = 300;
        clientPolicy.maxSocketIdle = 14;
        clientPolicy.useServicesAlternate = true;

        // Create connection to the database
        var client = new AerospikeClient(clientPolicy, new Host("localhost", 3100), new Host("localhost", 3101),
                new Host("localhost", 3102), new Host("localhost", 3103), new Host("localhost", 3104)); 

        // Deleting everything in test namespace 
        client.truncate(null, "test", null, null);

        var testKey = new Key("test", "demo", 22);
        var writePolicy = client.getWritePolicyDefault();
        writePolicy.maxRetries = 3;

        // inserts record
        client.put(null, testKey, new Bin("test", Value.get(100000)));

        var readPolicy = client.getReadPolicyDefault();
        readPolicy.timeoutDelay = 500;
        readPolicy.sendKey = true;
        
        var response = client.get(readPolicy, testKey);
        System.out.println(response.getInt("test"));
        client.close();





/* 
        var count = 1;

        var keys = new ArrayList<Key>();
        for (var i = 0; i < count; i++) {
            var key = new Key("test", "demo", i);
            client.put(null, key, new Bin("bin", Value.get(1000)));
            keys.add(key);
        }

        var transaction = new Txn();
        var policy = client.getReadPolicyDefault();
        policy.sendKey = true;
        policy.txn = transaction;

        for (Key key : keys) {
            try {
                client.get(policy, key);
            } catch (AerospikeException e) {
                System.out.printf("Failed to read record with key %s%n", key.userKey.toString());
            }
        }

        var anotherKey = new Key("test", "demo", 0);
        client.put(null, anotherKey, new Bin("bin", 999));
       
        var status = client.commit(transaction);
        if (status == CommitStatus.OK) {
            System.out.println("Transaction committed successfully.");
        } else {
            System.out.println("Transaction failed to commit.");
        }

        client.close();
        */
    }

}

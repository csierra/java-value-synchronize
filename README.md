# java-value-synchronize
A component to easily synchronize based on values

```java
final ValueSynchronizer<String> synchronizer = new ValueSynchronizer<>();

synchronizer.synchronizeOn(
	aString,
	s -> {
	  // isolated code
	});
```

This can be used with any values with consistent hash and equal.

You can also choose to clear some resource. If used again it would be generated but the clearing and creation are also isolated: 

```java
final ValueSynchronizer<String> synchronizer = new ValueSynchronizer<>(
	s -> {
		//isolated clearing code
	});

synchronizer.synchronizeOn(
	aString,
	s -> {
	  // isolated code
	});
	
synchronizer.clear(aString);
```

There is no need to clear the resources on every use.


# java-value-synchronize
A component to easily synchronize based on values

```
final ValueSynchronizer<String> synchronizer = new ValueSynchronizer<>();

synchronizer.synchronizeOn(
	aString,
	s -> {
	  // isolated code
	});

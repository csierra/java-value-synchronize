package com.bizonos.concurrent;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * @author Carlos Sierra Andrés
 */
public class ValueSynchronizer<T> {

	public ValueSynchronizer() {
		_clearConsumer = Optional.empty();
	}

	public ValueSynchronizer(Consumer<T> onClear) {
		_clearConsumer = Optional.ofNullable(onClear);
	}

	public void clear(T resource) {
		Object lock = _resourceLocks.get(resource);

		if (lock != null) {
			synchronized (lock) {
				if (_clearConsumer != null) {
					_clearConsumer.ifPresent(c -> c.accept(resource));
				}

				_resourceLocks.remove(resource, lock);
			}
		}
	}

	public void close() {
		if (_closed) {
			return;
		}

		_closed = true;

		if (_clearConsumer.isPresent()) {
			_resourceLocks.forEach((r, l) -> clear(r));
		}
	}

	public void synchronizeOn(T resource, Consumer<T> consumer) {
		if (_closed) {
			return;
		}

		Object lock = _resourceLocks.get(resource);

		if (lock == null) {
			Object newLock = new Object();

			lock = _resourceLocks.putIfAbsent(resource, newLock);

			if (lock == null) {
				lock = newLock;
			}
		}

		synchronized (lock) {
			if (lock != _resourceLocks.get(resource)) {
				return;
			}

			consumer.accept(resource);
		}
	}

	private final Optional<Consumer<T>> _clearConsumer;
	private final ConcurrentMap<T, Object> _resourceLocks =
		new ConcurrentHashMap<>();

	private volatile boolean _closed = false;

}

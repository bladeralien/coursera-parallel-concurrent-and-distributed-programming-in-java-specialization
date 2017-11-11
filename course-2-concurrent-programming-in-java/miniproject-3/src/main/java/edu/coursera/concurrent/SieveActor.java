package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 *
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determine the number of primes <= limit.
 */
public final class SieveActor extends Sieve {
    /**
     * {@inheritDoc}
     *
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */
    @Override
    public int countPrimes(final int limit) {
        SieveActorActor initial = new SieveActorActor(2);
        finish(() -> {
            for (int i = 3; i <= limit; i++) {
                initial.send(i);
            }
            initial.send(0);
        });
        int count = 1;
        SieveActorActor current = initial;
        while (current.next != null) {
            count += 1;
            current = current.next;
        }
        return count;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {
        private Integer localPrime;
        private SieveActorActor next;
        private SieveActorActor(int localPrime) {
            this.localPrime = localPrime;
        }
        /**
         * Process a single message sent to this actor.
         *
         * TODO complete this method.
         *
         * @param msg Received message
         */
        @Override
        public void process(final Object msg) {
            Integer candidate = (Integer) msg;
            if (candidate == 0) {
                if (next != null) {
                    next.send(candidate);
                }
                // TODO exit?
            }
            if (candidate % localPrime != 0) {
                if (next == null) {
                    next = new SieveActorActor(candidate);
                } else {
                    next.send(candidate);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(new SieveActor().countPrimes(17));
    }
}

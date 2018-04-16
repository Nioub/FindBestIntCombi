# FindBestIntCombi

## Origin

One day, some mess happened for our client : 
lots of items were not accounted properly in its information system,
there were lots of discrepancies between what had been sold and what had really been provided to users.

I was in the team in charge of the inventory, putting corresponding sums in front of items.
While the job was mostly automated with SQL queries, even Oracle had its limits.

More specifically, in few cases, there was 60 items without the corresponding sum, I had to find the "right" combination of items leading to the given sum.
At 60 items, the search space is big (2^60 = 1152921504606846976), so I wrote this algorithm (sorry, obvious legal issues prevent me to give the full program and data).
It computed the third of the search space on my desk computer in 1 week before finding the best combination.

## Design & implementation

Given a list l of n integers (n < 64 due to using longs) and an integer x to reach.

Find the combination of items from l whose sum equals x.
This combination must use the most items, for example given 1 2 3 6 to reach 6, 1 + 2 + 3 is the correct answer.

A vector of n bits is used as a binary comb for the presence of the n items in the sum.
We start by looking for the maximum number of n items, then all combinations of n-1 items, then n-2, etc down to 0.
At each stage, the next permutation is find by mutating the bit vector with a clever [Bit Twiddling Hack](http://graphics.stanford.edu/~seander/bithacks.html#NextBitPermutation).

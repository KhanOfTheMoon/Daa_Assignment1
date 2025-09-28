# Assignment 1 — Divide-and-Conquer Algorithms & Benchmarks (Merged Version)

This project implements classic divide-and-conquer algorithms, instruments them with safe recursion metrics, and validates theoretical recurrences (Master Theorem, Akra–Bazzi intuition) against measurements. Results are exported to CSV/Excel and visualized with plots.

## Architecture notes

* **Built-in instrumentation in each algorithm file**:
  comparisons counter, (optionally) **swaps/allocations** counter, wall-clock **time (ns/ms)**, and a **recursion-depth tracker** that increments on entry and decrements on exit; the maximum is recorded as `maxDepth`.
* **Metrics lifecycle**: counters reset per run; measurements written to CSV (block sections: `TimeMs`, `Comparisons`, `Allocations`, `MaxDepth`) and optionally to an Excel workbook with charts.
* **Memory discipline**: MergeSort uses **one reusable buffer** across recursion; QuickSort/Select are **in-place**.
* **Stack safety**: QuickSort always recurses on the **smaller** side and iterates over the larger (bounded stack, typically `O(log n)`).

## Algorithms & implementation notes

### MergeSort — `Θ(n log n)` (Master Case 2)

* **What it does**: splits the array into halves, sorts each half, then **linearly merges**.
* **Implementation details**: linear merge, **reusable buffer**, **small-n cutoff** to **insertion sort** (tiny subarrays are faster due to better cache and lower overhead).
* **Depth/allocations**: depth ≈ `⌊log₂ n⌋`; extra memory dominated by the shared buffer, not per-call allocations.

### QuickSort (robust) — average `Θ(n log n)`, worst-case `O(n²)`

* **What it does**: picks a **random pivot**, partitions into “smaller” and “bigger,” then **recurse on the smaller** part and **iterate** over the larger one.
* **Randomness & metrics**: we also **count swaps**; due to randomized pivot, **swap counts and time vary** between runs even on the same input.
* **Depth control**: “smaller-first” ensures a **bounded stack** (typ. `O(log n)`).

### Deterministic Select (Median-of-Medians, MoM5) — `Θ(n)`

* **What it does**: finds the **k-th smallest** element.
* **Implementation details**: **group by 5**, compute each group’s **median**, choose the **median of those medians** as the pivot, **in-place partition**, then **recurse only into the side** that contains the k-th element (prefer the smaller side).
* *(Note*: a true MoM pivot is the **median of medians**, **not** simply “the 5th element of the array.” Using a fixed array position would break the worst-case `Θ(n)` guarantee.)

### Closest Pair of Points (2D) — `Θ(n log n)` (Master Case 2)

* **What it does**: sort points by **x**, split recursively, then examine a vertical **strip** around the split line sorted by **y**; by the packing argument, only **~7–8 neighbors** per point need checking.
* **Practical note**: we also count “swaps”/operations where relevant for constant-factor insight. Despite the same `Θ(n log n)` as MergeSort, **constant factors** in the combine step make it typically the **slowest** wall-clock algorithm; e.g., minimum observed times around **6,000,000 ns (~6 ms)** on small instances.

## Recurrence analysis (brief)

* **MergeSort**: `T(n)=2T(n/2)+Θ(n)` → **`Θ(n log n)`** (Master Case 2).
* **QuickSort**: average `Θ(n log n)` with randomized pivot; worst `O(n²)`; stack bounded by smaller-first recursion.
* **Deterministic Select (MoM5)**: `T(n)=T(n/5)+T(7n/10)+Θ(n)` → **`Θ(n)`** (Akra–Bazzi intuition / CLRS).
* **Closest Pair (2D)**: `T(n)=2T(n/2)+Θ(n)` → **`Θ(n log n)`** (Master Case 2); strip work is linear with small constants.

## Measurements & plots (summary)

* **Time vs n**:

  * MergeSort and Closest Pair scale as **`n log n`**;
  * QuickSort is **`n log n` on average** but shows **run-to-run variance** from pivot randomness;
  * Deterministic Select scales **linearly**.
* **Depth vs n**: MergeSort/Closest Pair grow like **`log n`**; QuickSort typically stays near **`O(log n)`** with randomized pivot and smaller-first; Select’s depth is **small**.
* **Comparisons/allocations**:

  * For `n` doubling, sorting comparisons grow **roughly ~`n log n`** (often felt as “about doubles” in the measured range).
  * MergeSort’s allocations are dominated by the **single buffer**, not per-call churn; QuickSort/Select are **in-place** (near-zero allocations).
  * **Swaps** are tracked for QuickSort (and where applicable); they **vary with pivot** and affect constants and time.
* **Charts**:

  * *N vs Time* (clustered columns), *Allocations vs N* (columns), *Comparisons vs N* (horizontal bars), *Depth vs N* (line).
  * Data stored in CSV block sections and mirrored in an Excel workbook with the four charts on one sheet.

## Testing

* **Sorting**: correctness on random & adversarial arrays; verify QuickSort depth ≲ `~2·⌊log₂ n⌋ + O(1)` on average under randomized pivot.
* **Select**: compare against `Arrays.sort(a)[k]` across 100 random trials.
* **Closest Pair**: validate against an `O(n²)` checker for `n ≤ 2000`; use only the fast D&C version for large `n`.
* **Edge cases**: empty/singleton arrays, duplicates, presorted/reverse-sorted inputs.

## Summary

* **Randomized pivot** in QuickSort introduces **variance** in time, swaps, and depth across runs; average behavior is fast (`Θ(n log n)`), but the **worst case is `O(n²)`**.
* **MergeSort** is stable and efficient with **linear merge**, **reusable buffer**, and **small-n insertion sort**.
* **Deterministic Select** (group-of-5, median-of-medians) reliably achieves **linear time** with shallow recursion.
* **Closest Pair** matches `Θ(n log n)` asymptotically but is typically the **slowest** in wall-clock due to larger constants in the combine phase.
* Overall, **theory and measurements align**: sorters scale near `n log n`, Select is linear, and constant-factor effects (cache, branching, memory traffic, GC) explain small deviations—especially on tiny inputs.

/*
 * Given a sorted array, remove the duplicates in place such that each element appear only once and return the new length.

Do not allocate extra space for another array, you must do this in place with constant memory.

For example, Given input array nums = [1,1,2],

Your function should return length = 2, with the first two elements of nums being 1 and 2 respectively.
It doesn't matter what you leave beyond the new length.
 */

/*
 * Since the array is already sorted, we can keep two pointers ii and jj,
 * where ii is the slow-runner while jj is the fast-runner.
 *  As long as nums[i] = nums[j] , we increment jj to skip the duplicate.

When we encounter nums[j]≠nums[i], the duplicate run has ended
so we must copy its value to nums[i+1]. ii is then incremented and we repeat
 the same process again until jj reaches the end of array
 */

/*
 * Complexity analysis:

Assume that nn is the length of array. As each of ii and jj traverses at most nn steps, the time complexity is O(n)O(n).
 */
//int[] array=[1,1,2];

public int removeDuplicates(int[] nums)
{
	if(nums.length==0) return 0;

	int i=0;
	for(int j=1;j<nums.length;j++)
	{
		if(nums[i]!=nums[j]){
			i++
			nums[i+1]=nums[j];
		}
	}
	return i+1;
}

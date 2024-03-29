import java.util.*;

public class july_challenge {

    //  1 July 2023
    //  Fair distribution of cookies
    public int distributeCookies(int[] cookies, int k) {
        int[] minUnfairness = {Integer.MAX_VALUE};
        int[] allocation = new int[k];
        Arrays.fill(allocation,0);
        Arrays.sort(cookies); // Maybe sorting it makes it faster? Honestly idk

        backTracking(cookies, minUnfairness, k, allocation, 0);
        return minUnfairness[0];
    }

    private void backTracking(int[] cookies, int[] curMin, int k, int[] allocation, int index){
        
        //  All cookies have been allocated
        if (index == cookies.length){
            int curMax = Arrays.stream(allocation).max().orElse(Integer.MAX_VALUE);
            if (curMax < curMin[0]){
                curMin[0] = curMax;
            }
            return;
        }

        for (int i = 0; i < k; i++) {
            allocation[i] += cookies[index];
            
        //  Only continue if the allocation has less cookies than the current minimum
            if (allocation[i] <= curMin[0]){
               backTracking(cookies, curMin, k, allocation, index+1); 
            }
            allocation[i] -= cookies[index];
        }
    }

    //  2 July 2023
    //  Maximum number of achievable transfer requests
    public int maximumRequests(int n, int[][] requests) {

        //  Array to see net change in numbers for each building
        int[] allocation = new int[n];
        Arrays.fill(allocation, 0);

        //  HashSet to keep track of how many requests are processed
        HashSet<Integer> processed = new HashSet<>();

        int curMax[] = {0};
        backtrackRequest(requests, processed, curMax, allocation, 0);
        return curMax[0];
    }

    private void backtrackRequest(int[][] requests, HashSet<Integer> processed, int[] curMax, int[]allocation, int index){

        //  Reached the end of requests
        if (index == requests.length){
            for (int i = 0; i < allocation.length; i++) {
                if (allocation[i] != 0){
                    return;
                }
            }
            if (curMax[0] < processed.size()){
                curMax[0] = processed.size();
            }
            return;
        }

        //  Allow current allocation if net change for all buildings is 0
        boolean canAllocate = true;
        for (int i = 0; i < allocation.length; i++) {
            if (allocation[i] != 0){
                canAllocate = false;
                break;
            }
        }
        if (canAllocate){
            if (curMax[0] < processed.size()){
                curMax[0] = processed.size();
            }
        }

        for (int i = index; i < requests.length; i++) {
            allocation[requests[i][0]]--;
            allocation[requests[i][1]]++;
            processed.add(i);
            backtrackRequest(requests, processed, curMax, allocation, i+1);
            allocation[requests[i][0]]++;
            allocation[requests[i][1]]--;
            processed.remove(i);
        }
    }

    //  3 July 2023
    //  Buddy strings
    public boolean buddyStrings(String s, String goal) {

        //  Strings have different length
        if (s.length()!=goal.length()){
            return false;
        }

        //  Keep track of index of characters that are different
        int char1, char2;
        char1 = char2 = -1;

        //  Keep track of number of each letter in s
        Set<Character> letterCount = new HashSet<>();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != goal.charAt(i)){
                if (char1 == -1){
                    char1 = i;
                }
                else if (char2 == -1){
                    char2 = i;
                }
                else{
                    return false;
                }
            }
            letterCount.add(s.charAt(i));
        }

        //  Only one character is different, means no swaps can achieve desired state
        if (char1 != -1 && char2 == -1){
            return false;
        }

        //  s = goal
        if (char1 == -1 && char2 == -1){
            if (s.length() != letterCount.size()){
                return true;
            }
            return false;
        }

        //  2 characters are different
        if (s.charAt(char1) == goal.charAt(char2) && s.charAt(char2) == goal.charAt(char1)){
            return true;
        }

        return false;
    }

    //  4 July 2023
    //  Single number 2
    public int singleNumber(int[] nums) {
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            int sum = 0;
        
            //  For each num, by shifting it i bits to the right, we effectively count the total number of 1s at that bit
            //  Since most numbers appears exactly 3 times expect one, applying modulo of 3 can single out that number
            for (int num : nums){
                sum += num >> i & 1;
            }
            sum %= 3;
            ans |= sum << i;
        }
        return ans;
    }

    //  5 July 2023
    //  Longest Subarray of 1's After Deleting One Element
    public int longestSubarray(int[] nums) {
        //  Use of sliding window to update curMax

        int start, end, curMax, zeroCount;
        start = end = curMax = zeroCount = 0;

        //  Iterate through nums array
        while (end < nums.length){
            if (nums[end] == 1 && zeroCount <= 1){
                end++; // Expand the sliding window
            }
            else{
                if (zeroCount == 0){
                    zeroCount++;
                    end++;
                }
                else{
                    //  Update max sliding window
                    curMax = Math.max(curMax, (end - start));

                    //  Update start index until a zero is reached
                    while (nums[start] != 0){
                        start++;
                    }
                    start++;
                    zeroCount--;
                }
            }
        }
        return Math.max(curMax-1, end-start-1);
    }

    //  6 July 2023
    //  Minimum size subarray sum
    public int minSubArrayLen(int target, int[] nums) {
        int start, end, curSum, minCount, curCount;
        start = end = curSum = curCount = 0;
        minCount = Integer.MAX_VALUE;

        while (end < nums.length){
            if (curSum < target){
                curSum += nums[end++];
                curCount++;
            }
            else{
                minCount = Math.min(minCount, curCount);
                curSum -= nums[start++];
                curCount--;
            }
        }

        //  Decrease sliding window if sum > target
        while (curSum > target){
            curSum -= nums[start++];
            curCount--;
        }
        
        if (curSum >= target){
            return Math.min(minCount, curCount);
        }
        //  Sum less than target and whole array is traversed i.e. no answer
        else if (curCount == nums.length){
            return 0;
        }
        //  Sum less than target, so we add 1 and return the min between that and the current minimum
        else{
            return Math.min(minCount, curCount+1);

        }
    }  

    //  7 July 2023
    //  Maximize the confusion of an exam
    public int maxConsecutiveAnswers(String answerKey, int k) {
        //  Sliding window while keeping track of number of true and false

        int start, end, trueCount, falseCount, maxConsecutives;
        start = end = trueCount = falseCount = maxConsecutives = 0;

        char[] answerKeyArray = answerKey.toCharArray();
        while (end < answerKeyArray.length){
            if (trueCount <= k || falseCount <= k){
                if (answerKeyArray[end] == 'T'){
                    trueCount++;
                }
                else{
                    falseCount++;
                }
                end++;
            }
            else{
                maxConsecutives = Math.max(maxConsecutives, end - start - 1);
                while (trueCount > k && falseCount > k && start < answerKeyArray.length){
                    if (answerKeyArray[start] == 'T'){
                        trueCount--;
                    }
                    else{
                        falseCount--;
                    }
                    start++;
                }
            }
        }

        if (trueCount > k && falseCount > k){
            //  e.g. TFFT and k = 1, at the second T, it is still included because at that instance trueCount = 1
            return Math.max(maxConsecutives, end - start - 1);
        }
        return Math.max(maxConsecutives, end - start);
    }

    //  8 July 2023
    //  Put marbles in bags
    public long putMarbles(int[] weights, int k) {
        //  Assume if i partition at weights[i], the partition is between i and i+1
        //  e.g  2 3 4 5. and partition at 2 will give 2 | 3 4 5
        //  If we partition at index i, this will increase the sum by weights[i] + weights[i+1]

        int len = weights.length;

        if (len == k){
            return 0;
        }

        int[] sumIncrease = new int[len-1];

        for (int i = 0; i < len-1; i++) {
            sumIncrease[i] = weights[i] + weights[i+1];
        }
        
        Arrays.sort(sumIncrease);

        long minSum = 0;
        long maxSum = 0;

        for (int i = 0; i < k-1; i++) {
            minSum += sumIncrease[i];
            maxSum += sumIncrease[len-2-i];
        }
        
        return maxSum - minSum;

    }

    //  9 July 2023
    //  Substring with largest variance
    public int largestVariance(String s) {
        // Kadane's algorithm
        // I'm sorry this solution is mostly copied from online as I have non idea how to improve time complexity from 26^2 * N^2
        //  key considerations: a substring with only one kind of letter has variance 0, e.g. aaaa

        int[] freq = new int[26];
        for (int i = 0; i < s.length(); i++) {
            freq[(int)(s.charAt(i) - 'a')]++;
        }

        int maxVariance = 0;
        
        for (int a = 0; a < 26; a++) {
            for (int b = 0; b < 26; b++) {
                int remainingA = freq[a];
                int remainingB = freq[b];
                if (a == b || remainingA == 0 || remainingB == 0){
                    continue;
                }

                int curBfreq = 0;
                int curAfreq = 0;

                //  We assume for each letter pair (a,b), b needs to be appear more than a in order for maxVariance to get updated
                //  The opposite pair (b,a) will still get considered since the two for loops both runs from 0 through 25
                for (int i = 0; i < s.length(); i++) {
                    int c = (int)(s.charAt(i) - 'a');
                    if (c==b){
                        curBfreq++;
                    }
                    if (c==a){
                        curAfreq++;
                        remainingA--;
                    }
                    //  letter a needs to appear at least once or else it won't be a valid substring
                    if (curAfreq > 0){
                        maxVariance = Math.max(maxVariance, curBfreq -  curAfreq);
                    }

                    //  We only reset counter (i.e. finished considering substring up till current index) if b appears more than a, which gives negative variance
                    //  AND if there's still more letter a remaining, since that's required for a valid substring
                    if (curBfreq < curAfreq && remainingA > 0){
                        curBfreq = 0;
                        curAfreq = 0;
                    }
                    
                }
            }
        }
        return maxVariance;
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
        }
    }

    //  10 July 2023
    //  Minimum depth of binary tree
    public int minDepth(TreeNode root) {
    //  BFS to search for first leaf node
    //  Traverses each level at a time, and returns the first leaf node's depth

        if (root == null){
            return 0;
        }
        
        Queue<nodeQueue> queue = new LinkedList<>();
        queue.add(new nodeQueue(root, 1));
        while (!queue.isEmpty()){
            nodeQueue curNode = queue.poll();
            if (curNode.node.left == null && curNode.node.right == null){
                return curNode.depth;
            }
            if (curNode.node.left != null){
                queue.add(new nodeQueue(curNode.node.left, curNode.depth+1));
            }
            if (curNode.node.right != null){
                queue.add(new nodeQueue(curNode.node.right, curNode.depth+1));
            }
        }
        return 0;
    }

    public class nodeQueue{
        TreeNode node;
        int depth;
        nodeQueue(TreeNode node, int depth){
            this.node = node;
            this.depth = depth;
        }
    }

    //  11 July 2023
    //  All nodes distance k in binary tree
    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        List<Integer> result = new ArrayList<>();

        //  If node is the root
        if (root.val == target.val){
            treeDFS(root, 0, k, result);
            return result;
        }
        //  Find the target node and see whether it's in the left or right subtree of the root
        int[] depth = {1};
        TreeNode targetSubtree = findNode(root.left, target, depth);
        boolean targetInLeft = targetSubtree != null;
        if (!targetInLeft){
            depth[0] = 1;
            targetSubtree = findNode(root.right, target, depth);
        }
        System.out.println(depth[0]);

        //  Add root node if depth = k
        if (depth[0] == k){
            result.add(root.val);
        }

        //  If target is in the left subtree, we need to traverse the right subtree to find nodes with distance k - depth
        if (k >= depth[0]){
            if (targetInLeft){
                treeDFS(root.right, 1, k-depth[0], result);
            }
            //  Else traverse the left subtree to find nodes with distance k - depth
            else{
                treeDFS(root.left, 1, k-depth[0], result);
            }
        }
        

        TreeNode tempTarget = target;
        for (int i = 1; i < depth[0]; i++) {
            //  Move up the tree by 1 level each time and add all nodes with distance k - i + depth that is in the other subtree
            TreeNode parent = findParent(root, tempTarget);
            boolean inLeft = parent.left == tempTarget;
            tempTarget = parent;
            if (i == k){
                result.add(parent.val);
                break;
            }
            if (inLeft){
                treeDFS(parent.right, depth[0] - i + 1, k - i + depth[0] - i, result);
            }
            else{
                treeDFS(parent.left, depth[0] - i + 1, k - i + depth[0] - i, result);
            }
        }
        
        //  Add all nodes with distance k in a downward direction from target
        treeDFS(target, 0, k, result);
        return result;
    }

    //  Find target node and the depth it is located in
    public TreeNode findNode(TreeNode node, TreeNode target, int[] depth){
        if (node == null){
            return null;
        }
        if (node == target){
            return node;
        }
        
        TreeNode left = findNode(node.left, target, depth);
        TreeNode right = findNode(node.right, target, depth);
        if (left != null){
            depth[0]++;
            return left;
        }
        if (right != null){
            depth[0]++;
            return right;
        }
        return null;
    }

    //  Add all nodes of certain depth from a given node
    public void treeDFS(TreeNode node, int curDepth, int targetDepth, List<Integer> result){
        if (curDepth > targetDepth){
            return;
        }
        if (node == null){
            return;
        }
        if (curDepth == targetDepth){
            result.add(node.val);
            return;
        }
        treeDFS(node.left, curDepth+1, targetDepth, result);
        treeDFS(node.right, curDepth+1, targetDepth, result);
    }

    public TreeNode findParent (TreeNode node, TreeNode target){
        if (node == null){
            return null;
        }
        if (node.left == target || node.right == target){
            return node;
        }
        TreeNode left = findParent(node.left, target);
        TreeNode right = findParent(node.right, target);
        if (left != null){
            return left;
        }
        if (right != null){
            return right;
        }
        return null;
    }   

    //  12 July 2023
    // FInd eventual safe states
    public List<Integer> eventualSafeNodes(int[][] graph) {
        int length = graph.length;
        List<Integer> result = new ArrayList<>();
        List<Integer>[] reversedGraph = new List[length];
        int[] outgoingEdges = new int[length];
        int[] visited = new int[length];
        Queue<Integer> queue = new LinkedList<>();
    
        //  Reverse the graph
        //  With original graph, graph[i] shows all nodes that node i can reach
        //  With new graph, graph[i] shows all nodes that can reach node i
        for (int i = 0; i < length; i++) {
            reversedGraph[i] = new ArrayList<>();
        }
    
        for (int i = 0; i < length; i++) {
            for (int neighbor : graph[i]) {
                reversedGraph[neighbor].add(i);
                outgoingEdges[i]++;
            }
            if (outgoingEdges[i] == 0) {
                queue.offer(i); // Add nodes with zero incoming edges to the queue
                visited[i] = 1; // Mark them as visited
                result.add(i);  // Add them to the result list
            }
        }
    
        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int neighbor : reversedGraph[node]) {
                outgoingEdges[neighbor]--;
                if (outgoingEdges[neighbor] == 0 && visited[neighbor] != 1) {
                    queue.offer(neighbor);
                    visited[neighbor] = 1;
                    result.add(neighbor);
                }
            }
        }
    
        Collections.sort(result);
        return result;
    }

    //  13 July 2023
    //  Course schedule
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<Integer>[] graph = new List[numCourses];
        int courseFinished = 0;
        int[] outgoingEdges = new int[numCourses]; // Number of prerequisites for each course
        int[] visited = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList<>();
        }

        //  Graph[i] shows all courses that can be taken after course i is taken
        for (int i = 0; i < prerequisites.length; i++) {
            int preReq = prerequisites[i][1];
            int course = prerequisites[i][0];
            outgoingEdges[course]++;
            graph[preReq].add(course);
        }

        //  Add all courses with no prerequisites into a queue
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (outgoingEdges[i] == 0){
                queue.offer(i);
                visited[i] = 1;
            }
        }

        while (!queue.isEmpty()){
            int course = queue.poll();
            courseFinished++;
            for (int neighbor : graph[course]){
                outgoingEdges[neighbor]--;
                if (outgoingEdges[neighbor] == 0){
                    queue.offer(neighbor);
                    visited[neighbor] = 1;
                }
            }
        }
        
        if (courseFinished == numCourses){
            return true;
        }
        return false;
    }

    //  14 July 2023
    //Longest arithmetic subsequence of given difference
    public int longestSubsequence(int[] arr, int difference) {
        int SIZE = 20001;
        int[] dp = new int[SIZE];
        int max = 1;

        for (int i : arr){
            int index = i + 10000;
            int k = index - difference;
            if (k >= 0 && k < SIZE){
                dp[index] = dp[k] + 1;
                max = Math.max(max, dp[index]);
            }
            else{
                dp[index] = 1;
            }
        }
        
        return max;
    }

    //  15 July 2023
    //  Maximum number of events that can be attended II
    public int maxValue(int[][] events, int k) {
        int n = events.length;
        int[][] dp = new int[k + 1][n + 1];
        Arrays.sort(events, (a, b) -> a[0] - b[0]);

        //  dp[count][index] shows the maximum value that can be obtained by attending 'count' number of events starting from index 'index' to the end of events array
        for (int curIndex = n - 1; curIndex >= 0; curIndex--) {
            for (int count = 1; count <= k; count++) {
                int nextIndex = binarySearch(events, events[curIndex][1]);
                //  We can either choose to take this event, which will be events[curIndex][2] + dp[count - 1][nextIndex]
                //  OR
                //  We can choose to not take this event, which will be dp[count][curIndex + 1] i.e move on to next event
                dp[count][curIndex] = Math.max(dp[count][curIndex + 1], events[curIndex][2] + dp[count - 1][nextIndex]);
            }
        }
        return dp[k][0];
    }
    
    private int binarySearch(int[][] events, int target) {
        //  Searches for closest event with start date greater than target
        int left = 0;
        int right = events.length;
        while (left < right){
            int mid = left + (right - left) / 2;
            if (events[mid][0] <= target){
                left = mid + 1;
            }
            else{
                right = mid;
            }
        }
        return left;
    }

    //  16 July 2023
    //  Smallest sufficient team
    public int[] smallestSufficientTeam(String[] req_skills, List<List<String>> people) {
        int m = req_skills.length;
        int n = people.size();

        //  HashMap that maps each skill to a index
        HashMap<String, Integer> skillMap = new HashMap<>();
        for (int i = 0; i < m; i++){
            skillMap.put(req_skills[i], i);
        }

        //  peopleSkillMap[i] shows the skills that person i has in terms of which bit is set
        int[] peopleSkillMap = new int[n];
        for (int i = 0; i < n; i++) {
            for (String skill : people.get(i)) {
                peopleSkillMap[i] |= 1 << skillMap.get(skill);
            }
        }

        //  dp array to store results for all combinations of skills
        long[] dp = new long[1 << m];
        Arrays.fill(dp, Long.MAX_VALUE);
        dp[0] = 0; // When no skills are required, no people are required

        for (int i = 1; i < 1 << m; i++) { // Iterating through all combination of skill bit masks
            for (int j = 0; j < n; j++) { // Iterating through all people
                int skillsAlreadyPresent = i & ~peopleSkillMap[j]; //  Set of skills that the jth person don't have and thus need other people to have
                if (skillsAlreadyPresent != i){ // E.g. if i = 1111 and people[i] have skill 0000, this means skillsAlreadyPresent = 1111, and so we don't need people[i]
                    long peopleMask = dp[skillsAlreadyPresent] | (1L << j); //  People required for the current combination of skills
                    if (Long.bitCount(peopleMask) < Long.bitCount(dp[i])){
                        dp[i] = peopleMask;
                    }
                }
            }
        }

        long answerBitMask = dp[(1 << m) - 1];
        System.out.println(answerBitMask);
        int ans[] = new int[Long.bitCount(answerBitMask)];
        int index = 0;
        for (int i = 0; i < n; i++) {
            if (((answerBitMask >> i) & 1) == 1){
                ans[index++] = i;
            }
        }
        return ans;
    }

    public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    //  17 July 2023
    //  Add two numbers II
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        l1 = reverseLinkedList(l1);
        l2 = reverseLinkedList(l2);
    
        ListNode dummyHead = new ListNode(0);
        ListNode current = dummyHead;
        int carry = 0;
    
        while (l1 != null || l2 != null || carry != 0) {
            int sum = carry;
    
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
    
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
    
            carry = sum / 10;
            int digit = sum % 10;
            current.next = new ListNode(digit);
            current = current.next;
        }
    
        return reverseLinkedList(dummyHead.next);
    }
    
    private ListNode reverseLinkedList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
    
        while (current != null) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        return prev;
    }

    //  18 July 2023
    //  LRU cache
    public class Node{
        private int key;
        private int val;
        Node next;
        Node prev;

        public Node(int key, int val){
            this.key = key;
            this.val = val;
        }
    }
    public class LinkedList{
        private Node head;
        private Node tail;

        public void addToHead(Node node){
            if (head != null){
                node.next = head;
                head.prev = node;
            }
            if (tail == null){
                tail = node;
            }
            head = node;
        }

        public void unlink(Node node){
            if (node == null){
                return;
            }
            Node prevNode = node.prev;
            Node nextNode = node.next;

            if (prevNode != null){
                prevNode.next = nextNode;
            }
            if (nextNode != null){
                nextNode.prev = prevNode;
            }
            if (head == node){
                head = nextNode;
            }
            if (tail == node){
                tail = prevNode;
            }
            node.next = null;
            node.prev = null;
        }
    }

    class LRUCache {
        public int capacity;
        public HashMap<Integer, Node> cacheMap;
        public LinkedList history;

        public LRUCache(int capacity) {
            this.capacity = capacity;;
            cacheMap = new HashMap<>();
            history = new LinkedList();
        }
        
        public int get(int key) {
            if (!cacheMap.containsKey(key)){
                return -1;
            }
            Node node = cacheMap.get(key);
            if (history.head != node){
                history.unlink(node);
                history.addToHead(node);
            }     
            return node.val;
        }
        
        public void put(int key, int value) {
            Node node = new Node(key, value);

            if (cacheMap.containsKey(key)){
                removeItem(cacheMap.get(key));
            }

            if (cacheMap.size() >= capacity){
                removeOldest();
            }

            history.addToHead(node);
            cacheMap.put(key, node);
        }

        public void removeOldest(){
            Node node = history.tail;
            if (node == null){
                return;
            }
            removeItem(node);
        }

        public void removeItem(Node node){
            history.unlink(node);
            cacheMap.remove(node.key);
        }
    }

    //  19 July 2023
    //  Non-overlapping intervals
    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
    
        int count = 0;
        int end = Integer.MIN_VALUE;
    
        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i][0] < end) {
                // Current interval overlaps with the previous one
                count++;
                end = Math.min(end, intervals[i][1]); // Update the end to the smaller value, i.e remove the larger interval
            } else {
                // No overlap
                end = intervals[i][1];
            }
        }
    
        return count;
    }

    //  20 July 2023
    //  Asteroid collision
    public int[] asteroidCollision(int[] asteroids) {
        Stack<Integer> stack = new Stack<>();
        stack.push(asteroids[0]);

        int firstAsteroid, secondAsteroid;
        for (int i = 1; i < asteroids.length; i++) {
            if (!stack.isEmpty()){
                firstAsteroid = stack.pop();
                secondAsteroid = asteroids[i];
            }
            else{
                stack.push(asteroids[i]);
                continue;
            }
            
            //  While the 2 asteroids are colliding
            while (firstAsteroid > 0 && secondAsteroid < 0 && !stack.isEmpty()){
                if (Math.abs(firstAsteroid) == Math.abs(secondAsteroid)){ // equal size means both asteroids get destroyed
                    break;
                }
                else{
                    //  second asteroid is whatever remains from the collision
                    secondAsteroid = Math.abs(firstAsteroid) > Math.abs(secondAsteroid) ? firstAsteroid : secondAsteroid;
                    //  first asteroid is the next asteroid in the stack, which will be used to check if there is another collision
                    firstAsteroid = stack.pop();
                }
            }
            
            //  If not colliding
            if (firstAsteroid < 0 && secondAsteroid > 0 || firstAsteroid > 0 && secondAsteroid > 0 || firstAsteroid < 0 && secondAsteroid < 0){
                stack.push(firstAsteroid);
                stack.push(secondAsteroid);
            }

            //  If stack is empty and the 2 asteroids are colliding
            else{
                if (Math.abs(firstAsteroid) != Math.abs(secondAsteroid)){
                    stack.push(Math.abs(firstAsteroid) > Math.abs(secondAsteroid) ? firstAsteroid : secondAsteroid);
                }
            }
        }

        int[] result = new int[stack.size()];
        int i = result.length - 1;
        while (!stack.isEmpty()){
            result[i--] = stack.pop();
        }

        return result;
    }

    //  21 July 2023
    //  Number of longest increasing subsequence
    public int findNumberOfLIS(int[] nums) {
        int n = nums.length;

        //  length[i] shows the length of the longest increasing subsequence that ends at index i
        int[] length = new int[n];

        //  count[i] shows the number of longest increasing subsequence that ends at index i
        int[] count = new int[n];

        Arrays.fill(length, 1);
        Arrays.fill(count, 1);

        for (int i = 1; i < n; i++) {
            for (int j = i-1; j >= 0; j--) {
                if (nums[i] > nums[j]){ // we can increase the length of the subsequence by 1
                    if (length[i] < length[j] + 1){
                        length[i] = length[j] + 1;
                        count[i] = count[j];
                    }
                    else if (length[i] == length[j] + 1){
                        count[i] += count[j];
                    }
                }
            }
        }

        int maxLength = 0;
        for (int i = 0; i < n; i++) {
            maxLength = Math.max(maxLength, length[i]);
        }
        
        int result = 0;
        for (int i = 0; i < n; i++) {
            if (length[i] == maxLength){
                result += count[i];
            }
        }

        return result;
    }

    //  22 July 2023
    //  Knight probability in chessboard
    public double knightProbability(int n, int k, int row, int column) {
        double [][][] dp = new double[k+1][n][n];
        for (int i = 0; i < k+1; i++) {
            for (int j = 0; j < n; j++) {
                for (int j2 = 0; j2 < n; j2++) {
                    dp[i][j][j2] = 0;
                }
            }
        }
        dp[0][row][column] = 1;

        //  The idea is that the probability to move to a square is 1/8 the probability starting from the previous square
        for (int i = 1; i < k + 1; i++) {
            for (int j = 0; j < n; j++) {
                for (int y = 0; y < n; y++) {
                    //  2 rows up
                    if (j-2 >= 0){
                        //  1 column left
                        if (y-1 >= 0){
                            dp[i][j][y] += (1/8.0) * dp[i-1][j-2][y-1];
                        }
                        //  1 column right
                        if (y+1 < n){
                            dp[i][j][y] += (1/8.0) * dp[i-1][j-2][y+1];
                        }
                    }

                    //  2 rows down
                    if (j+2 < n){
                        //  1 column left
                        if (y-1 >= 0){
                            dp[i][j][y] += (1/8.0) * dp[i-1][j+2][y-1];
                        }
                        //  1 column right
                        if (y+1 < n){
                            dp[i][j][y] += (1/8.0) * dp[i-1][j+2][y+1];
                        }
                    }

                    //  2 columns right
                    if (y+2 < n){
                        //  1 row up
                        if (j-1 >= 0){
                            dp[i][j][y] += (1/8.0) * dp[i-1][j-1][y+2];
                        }
                        //  1 row down
                        if (j+1 < n){
                            dp[i][j][y] += (1/8.0) * dp[i-1][j+1][y+2];
                        }
                    }

                    //  2 columns left
                    if (y-2 >= 0){
                        //  1 row up
                        if (j-1 >= 0){
                            dp[i][j][y] += (1/8.0) * dp[i-1][j-1][y-2];
                        }
                        //  1 row down
                        if (j+1 < n){
                            dp[i][j][y] += (1/8.0) * dp[i-1][j+1][y-2];
                        }
                    }
                }
            }
        }

        double result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result += dp[k][i][j];
            }
        }
        return result;
    }

    //  23 July 2023
    //  All possible full binary trees
    public List<TreeNode> allPossibleFBT(int n) {
        HashMap<Integer, List<TreeNode>> memo = new HashMap<>();
        return recursiveAllPossibleFBT(n, memo);
    }

    public List<TreeNode> recursiveAllPossibleFBT(int n, HashMap<Integer, List<TreeNode>> memo){
        //  FBT = full binary tree, where all nodes have 0 or 2 child nodes
        //  FBT need to have a odd number of nodes, inclusive of the root node

        if (n%2 == 0){ // Not possible to have FBT with even number of nodes
            return new ArrayList<TreeNode>();
        }

        if (n == 1){ // A tree with one node is a FBT
            ArrayList<TreeNode> result = new ArrayList<TreeNode>();
            result.add(new TreeNode());
            return result;
        }

        if (memo.containsKey(n)){
            return memo.get(n);
        }

        List<TreeNode> result = new ArrayList<TreeNode>();
        for (int i = 1; i < n; i +=2){ // Split into the left subtree and right subtree. If the left tree has i nodes, the right tree will have n-i-1 nodes
            List<TreeNode> leftSubtrees = recursiveAllPossibleFBT(i, memo);
            List<TreeNode> rightSubtrees = recursiveAllPossibleFBT(n-i-1, memo);
            for (TreeNode left : leftSubtrees){
                for (TreeNode right : rightSubtrees){
                    //  Rebuilt the tree from the root node
                    TreeNode root = new TreeNode();
                    root.left = left;
                    root.right = right;
                    result.add(root);
                }
            }
        }

        memo.put(n, result);
        return result;
    }

    //  24 July 2023
    //  Pow(x, n)
    public double myPow(double x, int n) {
        return myPowWithLong(x, (long)n);
    }

    //  We use long instead of int to take care of edge case where n = -2147483648
    public double myPowWithLong(double x, long n){
        if (x == 1){
            return 1;
        }
        
        if (n == 0){
            return 1;
        }

        if (n < 0){
            return 1 / myPowWithLong(x, -n);
        }

        //  Suppose we have 4^50, this is the same as (4 x 4)^25, which is the same as 4 x ((4 x 4)^2)^12, and so on
        //  So we cut the power by half each time
        if (n % 2 == 0){
            return myPowWithLong(x*x, n/2);
        }

        else{
            return x * myPowWithLong(x*x, (n-1)/2);
        }
    }

    //  25 July 2023
    //  Peak index in a mountain array
    public int peakIndexInMountainArray(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        int mid;
    
        while (left < right) {
            mid = left + (right - left) / 2;
            
            if (mid < arr.length - 1 && arr[mid] < arr[mid + 1]) {
                // We are still in the increasing part of the array.
                left = mid + 1;
            } else if (mid > 0 && arr[mid] < arr[mid - 1]) {
                // We are in the decreasing part of the array.
                right = mid;
            } else {
                // We have found the peak element, or mid is at the boundary.
                return mid;
            }
        }
    
        // When the loop ends, left and right will converge to the peak element.
        return left;
    }

    //  26 July 2023
    //  Minimum speed to arrive on time
    public int minSpeedOnTime(int[] dist, double hour) {
        int left = 0;
        int right = 10000000;
        int mid;
        while (left < right){
            mid = left + (right - left) / 2;
            double time = timeRequired(dist, mid);
            if (time > hour){
                left = mid + 1;
            }
            else{
                right = mid;
            }
        }
        if (timeRequired(dist, left) <= hour){
            return left;
        }
        return -1;
    }

    //  Function to calculate the time required
    public double timeRequired(int[] dist, int speed){
        double time = 0;
        for (int i = 0; i < dist.length; i++) {
            if (i == dist.length - 1){
                time += (double)dist[i] / speed;
            }
            else{
                time += Math.ceil((double)dist[i] / speed);
            }
        }
        return time;
    }
    
    //  27 July 2023
    //  maximum running time of N computers
    public long maxRunTime(int n, int[] batteries) {
        long sum = 0;
        for (int i = 0; i < batteries.length; i++) {
            sum += batteries[i];
        }

        long left = 1;
        //  Upper bound happens when the batteries are spread out out equally among all computers, achieving maximum run time
        long right = sum/n;

        while (left < right){
            long mid = right - (right - left) / 2;
            if (canRunSimultaneously(n, batteries, mid)){
                left = mid;
            }
            else{
                right = mid - 1;
            }
        }
        return left;
    }

    public boolean canRunSimultaneously(int n, int[] batteries, long time){
        //  The idea is to find the total time required to run all computers for time t, and compare with the total power that the batteries can support
        long totalTime = 0;
        for (int i = 0; i < batteries.length; i++) {
            if (batteries[i] < time){ // Battery has less power than target time
                totalTime += batteries[i];
            }
            else{ // Battery has more power than target time
                //  Extra power from a battery won'e be included since doing so will require that battery to be swapped, which cannot be done since it is in use
                totalTime += time;
            }
        }
        return totalTime >= time*n;
    }

    //  28 July 2023
    //  Predict the winner
    public boolean PredictTheWinner(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][n];

        //  dp[i][j] represents the maximum difference between player 1 and player 2 when the subarray is nums[i:j]
        //  Base case: dp[i][i] = nums[i], since when nums has only one number, the max difference is the number itself
        for (int i = 0; i < dp.length; i++) {
            dp[i][i] = nums[i];
        }

        for (int diff = 1; diff < n; diff++) {
            for (int i = 0; i < n - diff; i++) {
                int j = i + diff;
                dp[i][j] = Math.max(nums[i] - dp[i+1][j], nums[j] - dp[i][j-1]);
            }
        }

        return dp[0][n-1] >= 0;
    }

    //  29 July 2023
    //  Soup servings
    public double soupServings(int n) {
        if (n >= 4800){ // Apparently when n is over 4800, the probability is extremely close to 1
            return 1;
        }

        HashMap<String, Double> memo = new HashMap<>();

        return soupServingsHelper(n, n, memo);
    }

    public double soupServingsHelper(int a, int b, HashMap<String, Double> memo){
        if (a <= 0 && b<= 0){
            return 0.5;
        }

        if (a <= 0){
            return 1;
        }

        if (b <= 0){
            return 0;
        }

        String key = a + "," + b;
        if (memo.containsKey(key)){
            return memo.get(key);
        }

        double p1 = 0.25 * soupServingsHelper(a-100, b, memo);
        double p2 = 0.25 * soupServingsHelper(a-75, b-25, memo);
        double p3 = 0.25 * soupServingsHelper(a-50, b-50, memo);
        double p4 = 0.25 * soupServingsHelper(a-25, b-75, memo);

        memo.put(key, p1 + p2 + p3 + p4);
        return memo.get(key);
    }

    //  30 July 2023
    //  Strange printer, btw i don't get this question at all this is the provided solution
    public int strangePrinter(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];

        for (int length = 1; length <= n; length++) {
            for (int left = 0; left <= n - length; left++) {
                int right = left + length - 1;
                int j = -1;
                dp[left][right] = n;
                for (int i = left; i < right; i++) {
                    if (s.charAt(i) != s.charAt(right) && j == -1){
                        j = i;
                    }
                    if (j != -1){
                        dp[left][right] = Math.min(dp[left][right], dp[j][i] + dp[i+1][right] + 1);
                    }
                }
                if (j == -1){
                    dp[left][right] = 0;
                }
            }
        }
        return dp[0][n-1] + 1;
    }

    //  31 July 2023
    //  Minimum ASCII delete sum for two strings
    public int minimumDeleteSum(String s1, String s2) {
        //  We're using the longest common subsequence algorithm, but instead of length, it uses the sum of ascii values
        int asciiSum = 0;
        for (int i = 0; i < s1.length(); i++) {
            asciiSum += s1.charAt(i);
        }
        for (int i = 0; i < s2.length(); i++) {
            asciiSum += s2.charAt(i);
        }

        int[][] dp = new int[s1.length()+1][s2.length()+1];
        for (int i = 0; i < s1.length() + 1; i++) {
            for (int j = 0; j < s2.length() + 1; j++) {
                if (i == 0 || j == 0){
                    dp[i][j] = 0;
                }
                else if (s1.charAt(i-1) == s2.charAt(j-1)){
                    dp[i][j] = dp[i-1][j-1] + s1.charAt(i-1);
                }
                else{
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
                }
            }
        }
        return asciiSum - 2 * dp[s1.length()][s2.length()];
    }
}
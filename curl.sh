curl -X POST --location "http://localhost:8081/api/account" \                                                                                                                                                 6 ✘  at 23:27:48  
-H "Content-Type: application/json" \
-d "{
\"accountNumber\": \"123345678\",
\"customerId\": \"12345\",
\"accountType\": \"CHECKING\",
\"initialDeposit\": 12.30
}"

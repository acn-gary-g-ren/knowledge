SELECT * FROM DRAFT_ITEM_VALUES
WHERE DELETE_FLAG = 0
ORDER BY INSERT_DATETIME %s
LIMIT ? OFFSET ?;
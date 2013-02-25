function prepareDeleteConfirmation(pId) {
    $('#deleter').click(function() {
        return confirm('投稿を削除していいですか？');
    });
}

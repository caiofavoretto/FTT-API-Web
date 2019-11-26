if (!localStorage.getItem('user')) {
  window.location = '/login';
} else {
  loadFeed('post-container');
}

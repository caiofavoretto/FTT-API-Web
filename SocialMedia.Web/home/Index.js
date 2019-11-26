if (!localStorage.getItem('userId')) {
  window.location = '/login';
} else {
  loadFeed('post-container');
}

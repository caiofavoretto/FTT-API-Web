const apiURL = 'http://localhost:8080';
const colors = [
  'FF4370',
  '7E1EAC',
  '291EAC',
  '2BBE2B',
  '2BBEB8',
  '842C66',
  '95B233',
];

async function handleCreateAccount(e) {
  e.preventDefault();

  const name = document.getElementById('name').value;
  const lastName = document.getElementById('lastname').value;
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  const confirmPassword = document.getElementById('confirm-password').value;

  if (name && lastName && email && password && password === confirmPassword) {
    const colorHex =
      colors[Math.floor(Math.random() * (colors.length - 1)) + 1];
    try {
      const response = await axios.post(`${apiURL}/users`, {
        name,
        lastName,
        email,
        password,
        colorHex,
      });
      console.log(response.data);

      window.location = '/login';
    } catch (error) {
      alert(error.response.data.error);
    }
  } else {
    alert('Preencha os campos corretamente');
  }
}

async function handleLogin(e) {
  e.preventDefault();

  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  if (email && password) {
    try {
      const response = await axios.post(`${apiURL}/users/login`, {
        email,
        password,
      });

      localStorage.setItem('user', JSON.stringify(response.data));
      window.location = '/home';
    } catch (error) {
      alert(error.response.data.error);
    }
  } else {
    alert('Preencha os campos corretamente');
  }
}

// Profile
function generateProfile() {
  const profile = document.getElementById('profile');

  const loggedUser = JSON.parse(localStorage.getItem('user'));

  const html = /*html*/ `
    <a href="/profile">
      <div class="icon profile" style="background: #${loggedUser.colorHex};">
      ${loggedUser.name[0].toUpperCase() + loggedUser.lastName[0].toUpperCase()}
      </div>
      Perfil
    </a>
  `;

  profile.insertAdjacentHTML('afterbegin', html);
}

// FEED
async function loadFeed(elementId) {
  try {
    const response = await axios.get(`${apiURL}/posts`);
    const parent = document.getElementById(elementId);

    generateProfile();

    const loggedUser = JSON.parse(localStorage.getItem('user'));
    const profilePost = document.getElementById('profile-post');

    profilePost.style.background = `#${loggedUser.colorHex}`;
    profilePost.innerHTML = `${loggedUser.name[0].toUpperCase() +
      loggedUser.lastName[0].toUpperCase()}`;

    response.data.map(post => {
      const html = /*html*/ `
        <section onclick="loadPost('${post.id}')" id="post-${
        post.id
      }" class="post">
          <div class="profile" style="background: #${post.user.colorHex};">
            ${post.user.name[0].toUpperCase() +
              post.user.lastName[0].toUpperCase()}
          </div>
          <div class="post-content">
            <div class="content">
              <strong class="post-name">
                ${post.user.name} ${post.user.lastName} 
                ${
                  post.title
                    ? /*html*/ `<span class="post-title">- ${post.title}</span>`
                    : ''
                }
              </strong>
              <p class="message">
                ${post.description}
              </p>
            </div>

            <!-- Actions -->
            <div class="actions">
              <div class="btn-labeled">
                <button onclick="like('${post.id}')">
                  <svg
                    class="like-icon icon"
                    xmlns="http://www.w3.org/2000/svg"
                    width="26"
                    height="24"
                    viewBox="0 0 26 24"
                  >
                    <path
                      id="Heart"
                      class="cls-1"
                      d="M-2731.2-559.4h0l-.057-.045c-3.4-2.669-10.739-8.7-10.739-15.007a6.546,6.546,0,0,1,6.546-6.546,7.13,7.13,0,0,1,5.455,3,7.13,7.13,0,0,1,5.455-3,6.545,6.545,0,0,1,6.545,6.546c0,6.3-7.337,12.337-10.738,15.007l-.057.045h0a1.99,1.99,0,0,1-1.2.4A1.99,1.99,0,0,1-2731.2-559.4Z"
                      transform="translate(2743 582)"
                    />
                  </svg>
                </button>
                <strong>${post.likes}</strong>
              </div>

              <div class="btn-labeled">
                <button onclick="openCommentOverlay('${post.id}')">
                  <svg
                    class="comment-icon icon"
                    xmlns="http://www.w3.org/2000/svg"
                    width="25"
                    height="22.665"
                    viewBox="0 0 25 22.665"
                  >
                    <g id="Comment" class="cls-1" transform="translate(1990)">
                      <path
                        class="cls-2"
                        d="M -1969.058959960938 21.26299476623535 L -1972.72265625 19.79463005065918 L -1973.015625 19.6772403717041 L -1973.322875976562 19.74923133850098 C -1974.0322265625 19.91543006896973 -1974.7646484375 19.99970054626465 -1975.5 19.99970054626465 L -1979.499633789062 19.99970054626465 C -1984.738159179688 19.99970054626465 -1989 15.73829078674316 -1989 10.50030040740967 C -1989 5.261820793151855 -1984.738159179688 1.000000715255737 -1979.499633789062 1.000000715255737 L -1975.5 1.000000715255737 C -1970.261596679688 1.000000715255737 -1965.999755859375 5.261820793151855 -1965.999755859375 10.50030040740967 C -1965.999755859375 12.74246120452881 -1966.795654296875 14.91887092590332 -1968.240844726562 16.62859153747559 L -1968.42626953125 16.84796142578125 L -1968.467041015625 17.13230133056641 L -1969.058959960938 21.26299476623535 Z"
                      />
                      <path
                        class="cls-3"
                        d="M -1969.868286132812 19.86129760742188 L -1969.456909179688 16.99044990539551 L -1969.37548828125 16.42178153991699 L -1969.004638671875 15.98304080963135 C -1967.711791992188 14.45357036590576 -1966.999755859375 12.50643062591553 -1966.999755859375 10.50030040740967 C -1966.999755859375 5.813220500946045 -1970.81298828125 2.000000715255737 -1975.5 2.000000715255737 L -1979.499633789062 2.000000715255737 C -1984.186767578125 2.000000715255737 -1988 5.813220500946045 -1988 10.50030040740967 C -1988 15.18689060211182 -1984.186767578125 18.99970054626465 -1979.499633789062 18.99970054626465 L -1975.5 18.99970054626465 C -1974.84130859375 18.99970054626465 -1974.185668945312 18.92430114746094 -1973.551025390625 18.77560043334961 L -1972.9365234375 18.63162040710449 L -1972.350708007812 18.86641120910645 L -1969.868286132812 19.86129760742188 M -1968.249633789062 22.66470146179199 L -1973.0947265625 20.72286033630371 C -1973.867065429688 20.90383148193359 -1974.672607421875 20.99970054626465 -1975.5 20.99970054626465 L -1979.499633789062 20.99970054626465 C -1985.299194335938 20.99970054626465 -1990 16.29899978637695 -1990 10.50030040740967 C -1990 4.700700759887695 -1985.299194335938 7.308959766305634e-07 -1979.499633789062 7.308959766305634e-07 L -1975.5 7.308959766305634e-07 C -1969.701293945312 7.308959766305634e-07 -1964.999755859375 4.700700759887695 -1964.999755859375 10.50030040740967 C -1964.999755859375 13.08183097839355 -1965.931640625 15.44574069976807 -1967.477172851562 17.27415084838867 L -1968.249633789062 22.66470146179199 Z"
                      />
                    </g>
                  </svg>
                </button>
                <strong>${post.comments}</strong>
              </div>
            </div>
          </div>
        </section>
      `;
      parent.insertAdjacentHTML('afterbegin', html);
    });
  } catch (error) {
    alert(error.response.data.error);
  }
}

// POST
async function post() {
  const description = document.getElementById('post-textarea').value;
  const title = document.getElementById('post-title-input').value;

  const userId = JSON.parse(localStorage.getItem('user')).id;

  if (description) {
    try {
      const response = await axios.post(
        `${apiURL}/posts`,
        {
          title,
          description,
        },
        {
          headers: {
            userId,
          },
        },
      );
      window.location = '/home';
    } catch (error) {
      console.log(error.data);
      alert(error.response.data.error);
    }
  }
}

function onPostContentChange(e) {
  const button = document.getElementById('post-content-btn');
  if (e.target.value) {
    button.disabled = false;
  } else {
    button.disabled = true;
  }
}

function enableTitle(element) {
  const inputTitle = document.getElementById('post-title-input');
  if (element.classList.contains('active')) {
    inputTitle.classList.add('hidden');
    element.classList.remove('active');
    inputTitle.value = '';
  } else {
    inputTitle.classList.remove('hidden');
    element.classList.add('active');
  }
}

function loadPost(postId) {
  window.location = `/posts/?postId=${postId}`;
}

// COMMENTS
async function loadPostComments(postElementId, commentsElementId) {
  const urlParams = new URLSearchParams(window.location.search);
  const postId = urlParams.get('postId');
  const loggedUser = JSON.parse(localStorage.getItem('user'));

  try {
    const [postData, commentsData] = await Promise.all([
      axios.get(`${apiURL}/posts/user/${postId}`),
      axios.get(`${apiURL}/comments/${postId}`),
    ]);

    const postParent = document.getElementById(postElementId);
    const commentsParent = document.getElementById(commentsElementId);

    const htmlPost = /*html*/ `
      <section id="post-${postData.data.id}" class="post">
        <div class="post-author">
          <div class="profile" style="background: #${
            postData.data.user.colorHex
          };">
            ${postData.data.user.name[0].toUpperCase() +
              postData.data.user.lastName[0].toUpperCase()}
          </div>
          <strong class="post-name">
            ${postData.data.user.name} ${postData.data.user.lastName} 
            ${
              postData.data.title
                ? /*html*/ `<span class="post-title">- ${postData.data.title}</span>`
                : ''
            }
          </strong>
        </div>
        <p class="message">${postData.data.description}</p>

        <!-- Actions -->
        <div class="actions">
          <div class="btn-labeled">
            <button onclick="like('${postId}')">
              <svg
                class="like-icon icon"
                xmlns="http://www.w3.org/2000/svg"
                width="26"
                height="24"
                viewBox="0 0 26 24"
              >
                <path
                  id="Heart"
                  class="cls-1"
                  d="M-2731.2-559.4h0l-.057-.045c-3.4-2.669-10.739-8.7-10.739-15.007a6.546,6.546,0,0,1,6.546-6.546,7.13,7.13,0,0,1,5.455,3,7.13,7.13,0,0,1,5.455-3,6.545,6.545,0,0,1,6.545,6.546c0,6.3-7.337,12.337-10.738,15.007l-.057.045h0a1.99,1.99,0,0,1-1.2.4A1.99,1.99,0,0,1-2731.2-559.4Z"
                  transform="translate(2743 582)"
                />
              </svg>
            </button>
            <strong>${postData.data.likes}</strong>
          </div>

          <div class="btn-labeled">
            <button onclick="openCommentOverlay('${postId}')">
              <svg
                class="comment-icon icon"
                xmlns="http://www.w3.org/2000/svg"
                width="25"
                height="22.665"
                viewBox="0 0 25 22.665"
              >
                <g id="Comment" class="cls-1" transform="translate(1990)">
                  <path
                    class="cls-2"
                    d="M -1969.058959960938 21.26299476623535 L -1972.72265625 19.79463005065918 L -1973.015625 19.6772403717041 L -1973.322875976562 19.74923133850098 C -1974.0322265625 19.91543006896973 -1974.7646484375 19.99970054626465 -1975.5 19.99970054626465 L -1979.499633789062 19.99970054626465 C -1984.738159179688 19.99970054626465 -1989 15.73829078674316 -1989 10.50030040740967 C -1989 5.261820793151855 -1984.738159179688 1.000000715255737 -1979.499633789062 1.000000715255737 L -1975.5 1.000000715255737 C -1970.261596679688 1.000000715255737 -1965.999755859375 5.261820793151855 -1965.999755859375 10.50030040740967 C -1965.999755859375 12.74246120452881 -1966.795654296875 14.91887092590332 -1968.240844726562 16.62859153747559 L -1968.42626953125 16.84796142578125 L -1968.467041015625 17.13230133056641 L -1969.058959960938 21.26299476623535 Z"
                  />
                  <path
                    class="cls-3"
                    d="M -1969.868286132812 19.86129760742188 L -1969.456909179688 16.99044990539551 L -1969.37548828125 16.42178153991699 L -1969.004638671875 15.98304080963135 C -1967.711791992188 14.45357036590576 -1966.999755859375 12.50643062591553 -1966.999755859375 10.50030040740967 C -1966.999755859375 5.813220500946045 -1970.81298828125 2.000000715255737 -1975.5 2.000000715255737 L -1979.499633789062 2.000000715255737 C -1984.186767578125 2.000000715255737 -1988 5.813220500946045 -1988 10.50030040740967 C -1988 15.18689060211182 -1984.186767578125 18.99970054626465 -1979.499633789062 18.99970054626465 L -1975.5 18.99970054626465 C -1974.84130859375 18.99970054626465 -1974.185668945312 18.92430114746094 -1973.551025390625 18.77560043334961 L -1972.9365234375 18.63162040710449 L -1972.350708007812 18.86641120910645 L -1969.868286132812 19.86129760742188 M -1968.249633789062 22.66470146179199 L -1973.0947265625 20.72286033630371 C -1973.867065429688 20.90383148193359 -1974.672607421875 20.99970054626465 -1975.5 20.99970054626465 L -1979.499633789062 20.99970054626465 C -1985.299194335938 20.99970054626465 -1990 16.29899978637695 -1990 10.50030040740967 C -1990 4.700700759887695 -1985.299194335938 7.308959766305634e-07 -1979.499633789062 7.308959766305634e-07 L -1975.5 7.308959766305634e-07 C -1969.701293945312 7.308959766305634e-07 -1964.999755859375 4.700700759887695 -1964.999755859375 10.50030040740967 C -1964.999755859375 13.08183097839355 -1965.931640625 15.44574069976807 -1967.477172851562 17.27415084838867 L -1968.249633789062 22.66470146179199 Z"
                  />
                </g>
              </svg>
            </button>
            <strong>${postData.data.comments}</strong>
          </div>
        </div>
      </section>
    `;

    postParent.insertAdjacentHTML('afterbegin', htmlPost);

    if (commentsData.data.length > 0) {
      commentsData.data.map(comment => {
        const html = /*html*/ `
          <section id="comment-${comment.id}" class="comment">
            <div class="profile" style="background: #${comment.user.colorHex};">
            ${comment.user.name[0].toUpperCase() +
              comment.user.lastName[0].toUpperCase()}
            </div>
            <div class="comment-content">
              <div class="content">
                <strong class="comment-name">
                ${comment.user.name} ${comment.user.lastName} 
                </strong>
                <p class="message">
                  ${comment.description}
                </p>
              </div>
            </div>
          </section>
        `;

        commentsParent.insertAdjacentHTML('afterbegin', html);
      });
    }

    if (loggedUser) {
    }
  } catch (error) {
    alert(error.response.data.error);
  }
}

async function openCommentOverlay(postId) {
  if (!e) var e = window.event;
  e.cancelBubble = true;
  if (e.stopPropagation) e.stopPropagation();

  const overlay = document.getElementById('comment-overlay');

  const loggedUser = JSON.parse(localStorage.getItem('user'));

  try {
    debugger;
    const postData = await axios.get(`${apiURL}/posts/user/${postId}`);

    if (loggedUser) {
      overlay.classList.remove('hidden');

      const htmlOverlay = /*html*/ `
          <div class="container">
            <div class="close">
              <button
                onclick="closeCommentOverlay()"
                class="btn-back"
                type="button"
              >
                <img width="28" src="../assets/Back.svg" alt="BackButton" />
              </button>
            </div>
            <div class="post-comment">
              <section class="post">
                <div class="profile" style="background: #${
                  postData.data.user.colorHex
                };">
                ${postData.data.user.name[0].toUpperCase() +
                  postData.data.user.lastName[0].toUpperCase()}
                </div>
                <div class="post-content">
                  <div class="content">
                    <strong class="post-name">
                    ${postData.data.user.name} ${postData.data.user.lastName} 
                    ${
                      postData.data.title
                        ? /*html*/ `<span class="post-title">- ${postData.data.title}</span>`
                        : ''
                    }
                    </strong>
                    <p class="message">${postData.data.description}</p>
                  </div>
                </div>
              </section>
              <div class="comment">
                <div class="profile" style="background: #${
                  loggedUser.colorHex
                };">
                  ${loggedUser.name[0].toUpperCase() +
                    loggedUser.lastName[0].toUpperCase()}
                </div>
                <input oninput="onCommentContentChange(event)" placeholder="Responder..." id="comment-input" />
              </div>
              <div class="actions">
                <button 
                onclick="comment('${postData.data.id}')" 
                id="comment-content-btn" 
                disabled class="btn-action">
                  COMMENT
                </button>
              </div>
            </div>
          </div>
        `;

      overlay.insertAdjacentHTML('afterbegin', htmlOverlay);
    } else {
      alert('Você precisa estar logado para comentar.');
    }
  } catch (error) {
    alert(error.data.error);
  }
}

function closeCommentOverlay() {
  const overlay = document.getElementById('comment-overlay');
  overlay.classList.add('hidden');
}

function onCommentContentChange(e) {
  const button = document.getElementById('comment-content-btn');
  if (e.target.value) {
    button.disabled = false;
  } else {
    button.disabled = true;
  }
}

async function comment(postId) {
  const description = document.getElementById('comment-input').value;

  const userId = JSON.parse(localStorage.getItem('user')).id;

  if (description) {
    try {
      const response = await axios.post(
        `${apiURL}/comments/${postId}`,
        {
          description,
        },
        {
          headers: {
            userId,
          },
        },
      );
      window.location = `/posts/?postId=${postId}`;
    } catch (error) {
      console.log(error.data);
      alert(error.response.data.error);
    }
  }
}

// LIKE
async function like(postId) {
  if (!e) var e = window.event;
  e.cancelBubble = true;
  if (e.stopPropagation) e.stopPropagation();

  const userId = JSON.parse(localStorage.getItem('user')).id;

  try {
    const response = await axios.post(
      `${apiURL}/likes/${postId}`,
      {},
      {
        headers: {
          userId,
        },
      },
    );

    if (window.location.pathname.includes('home')) {
      window.location = `/home`;
    } else {
      window.location = `/posts/?postId=${postId}`;
    }
  } catch (error) {
    alert(error.response.data.error);
  }
}

// Log out
function logOut() {
  localStorage.clear();
  window.location = '/login';
}

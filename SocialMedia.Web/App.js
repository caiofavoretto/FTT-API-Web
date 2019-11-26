const apiURL = 'http://localhost:8080';

// const [userData, reposData] = await Promise.all([
//   axios.get(`https://api.github.com/users/${inputValue}`),
//   axios.get(`https://api.github.com/users/${inputValue}/repos?sort=stars`)
// ]);

async function handleCreateAccount(e) {
  e.preventDefault();

  const name = document.getElementById('name').value;
  const lastName = document.getElementById('lastname').value;
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  const confirmPassword = document.getElementById('confirm-password').value;

  if (name && lastName && email && password && password === confirmPassword) {
    try {
      const response = await axios.post(`${apiURL}/users`, {
        name,
        lastName,
        email,
        password,
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

      localStorage.setItem('userId', response.data.id);
      window.location = '/home';
    } catch (error) {
      alert(error.response.data.error);
    }
  } else {
    alert('Preencha os campos corretamente');
  }
}

async function loadFeed(elementId) {
  try {
    const response = await axios.get(`${apiURL}/posts`);
    const parent = document.getElementById(elementId);
    response.data.map(post => {
      const html = /*html*/ `
        <section id="post-${post.id}" class="post">
          <div class="profile" style="background: #7E1EAC;">
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
                <button>
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
                <button>
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
      parent.insertAdjacentHTML('beforeend', html);
    });
  } catch (error) {
    alert(error.response.data.error);
  }
}

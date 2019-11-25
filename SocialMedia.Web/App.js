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
        password
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

  debugger;

  if (email && password) {
    try {
      const response = await axios.post(`${apiURL}/users/login`, {
        email,
        password
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

// routes
const Home = {
    template: `
    <div>
        <h2>Home</h2>
        <p>hello</p>
    </div>
    `
};

const Login = {
    template: `
        <div class="container">
            <form class="form-signin">
                <h2 class="form-signin-heading">Please sign in</h2>
                <el-input v-model="username" placeholder="用户名"></el-input><p></p>
                <el-input type="password" v-model="password" placeholder="密码"></el-input><p></p>
            	<el-checkbox v-model="rememberMe">Remember me</el-checkbox><br>
                <el-button @click="login" type="primary">登录</el-button>
            </form>
        </div>
    `,
    data() {
        return {
            username: 'hello',
            password: 'test',
            rememberMe: false
        }
    },
    methods: {
        login: function (event) {
            let body = `username=${this.username}&password=${this.password}&rememberMe=${this.rememberMe}`;

            fetch('/login', {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;'
                },
                body
            }).then(resp => {
                return resp.json();
            }).then(retMsg => {
                console.log(retMsg);
                if (retMsg.code === '0000') {
                    this.$notify({
                        title: '成功',
                        message: '登录成功',
                        type: 'success'
                    });
                } else {
                    this.$notify({
                        message: retMsg.msg,
                        type: 'warning'
                    });
                }
            });
        }
    }
};


// router
const router = new VueRouter({
    routes: [
        { path: '/', name: 'home', component: Home },
        { path: '/login', name: 'login', component: Login }
    ]
});

// vue app
const app = new Vue({
    el: '#app',
    template: `
        <div>
            <ul>
                <li>
                    <router-link to="/">/</router-link>
                </li>
                <li>
                    <router-link to="/login">登录</router-link>
                </li>
                <li>
                    <a href="#" @click="logout">退出</a>
                </li>
            </ul>
            <router-view></router-view>
        </div>
    `,
    methods: {
        logout: function() {
            fetch('/logout').then(res => {
                return res.json();
            }).then(ret => {
                this.$message('已经成功退出！');
            });
        }
    },
    router
});
const app = Vue.createApp({
    data(){
        return{
            firstName: '',
            lastName: '',
            email: '',
            password: '',
            rptPassword: '',
        }
    },

    created() {
        
    },

    methods: {
        login(){
            axios.post('/api/login', `email=${this.email}&password=${this.password}`)
            .then(() => window.location.assign('../web/accounts.html'))
            .catch(error => {
                let errorData = error.response.data
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `${errorData}`
                })
            })

            this.resetInput()
        },

        register(){
            if(this.password == this.rptPassword){
                axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`)
                .then(() => this.login())
                .catch(error => {
                    let errorData = error.response.data
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: `${errorData}`
                    })
                })
            }
            else {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Passwords are not the same'
                })
            }
        },

        // .catch(function (error) {
        //     let errorData = error.response.data
        //     Swal.fire({
        //         icon: 'error',
        //         title: 'Oops...',
        //         text: ${errorData}.,
        //     })
        // })

        resetInput(){
            this.firstName = ''
            this.lastName = ''
            this.email = ''
            this.password = ''
            this.rptPassword = ''
        },

    },
    
}).mount('#app')

// axios.post('/api/login',"email=melba@mindhub.com&password=1234").then(response => console.log('signed in!!!'))
// axios.post('/api/logout').then(response => console.log('signed out!!!'))

//axios.post('/api/clients',"firstName=pedro2&lastName=rodriguez&email=pedro@mindhub.com&password=pedro").then(response => console.log('registered'))
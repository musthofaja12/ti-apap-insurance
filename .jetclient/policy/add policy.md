```toml
name = 'add policy'
method = 'POST'
url = '{{url-insurance}}/policy/create?nik=1'
sortWeight = 3000000
id = '70f234f0-382a-4b1b-a7c2-5ae4d21d6fe3'

[[queryParams]]
key = 'nik'
value = '1'

[body]
type = 'JSON'
raw = '''
{
  company: "d9dd4751-c13b-433c-a56f-fa098f5dc2ad",
  expiryDate: "2024-12-12"
}'''
```
